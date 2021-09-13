package com.icanmobile.openflickrviewer.repository

import com.icanmobile.openflickrviewer.model.toPhotoList
import com.icanmobile.openflickrviewer.repository.api.FakeFlickrApi
import com.icanmobile.openflickrviewer.repository.api.FlickrDTO
import com.icanmobile.openflickrviewer.repository.api.toFlickerDTO
import com.icanmobile.openflickrviewer.repository.db.FakeFlickrDAO
import com.icanmobile.openflickrviewer.repository.db.toFlickrEntityList
import com.icanmobile.openflickrviewer.ui.viewmodel.state.ViewState
import com.icanmobile.openflickrviewer.ui.viewmodel.state.ViewStateEvent
import com.icanmobile.openflickrviewer.util.ApiResponseHandler
import com.icanmobile.openflickrviewer.util.DataState
import com.icanmobile.openflickrviewer.util.StateEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeFlickrRepositoryImpl @Inject constructor(): FlickrRepository{

    lateinit var flickrApi: FakeFlickrApi
    lateinit var flickrDao: FakeFlickrDAO

    private fun throwExceptionIfApiAndDAONotInitialized(){
        if(!::flickrApi.isInitialized) {
            throw UninitializedPropertyAccessException(
                "Did you forget to set the flickrApi in FakeMainRepositoryImpl?"
            )
        }

        if (!::flickrDao.isInitialized) {
            throw UninitializedPropertyAccessException(
                "Did you forget to set the flickrDao in FakeMainRepositoryImpl?"
            )
        }
    }

    private var currentPage: AtomicInteger = AtomicInteger()

    @Throws(UninitializedPropertyAccessException::class)
    override fun searchPhotos(
        stateEvent: StateEvent,
        query: String
    ): Flow<DataState<ViewState>> {
        throwExceptionIfApiAndDAONotInitialized()

        return flow {
            val page = if (stateEvent is ViewStateEvent.SearchMorePhotosEvent) {
                currentPage.incrementAndGet()
            } else {
                currentPage.set(1)
                currentPage.get()
            }

            val response = safeApiCall(Dispatchers.IO){ flickrApi.searchPhotos(query, page) }
            emit(
                object: ApiResponseHandler<ViewState, FlickrDTO>(
                    response = response,
                    stateEvent = stateEvent
                ) {
                    override fun handleSuccess(flickrDTO: FlickrDTO): DataState<ViewState> {
                        val resultObj = runBlocking {
                            flickrDTO.toFlickrEntityList(query)
                                .takeIf { !it.isNullOrEmpty() }
                                ?.run { flickrDao.insertPhotos(this!!) }
                                .let { flickrDao.getPhotos(query) }
                                .run { toFlickerDTO() }
                        }
                        return getDataState(resultObj)
                    }

                    override fun handleNetworkError(): DataState<ViewState> {
                        val resultObj = runBlocking { flickrDao.getPhotos(query).toFlickerDTO() }
                        return getDataState(resultObj)
                    }

                    private fun getDataState(resultObj: FlickrDTO): DataState<ViewState> {
                        return DataState.data(
                            data = ViewState(
                                photoListFragmentView = ViewState.PhotoListFragmentView(
                                    photos = resultObj.toPhotoList()
                                )
                            ),
                            stateEvent = stateEvent
                        )
                    }
                }.result
            )
        }
    }
}