package com.icanmobile.openflickrviewer.repository

import com.icanmobile.openflickrviewer.model.toPhotoList
import com.icanmobile.openflickrviewer.repository.api.FlickrApi
import com.icanmobile.openflickrviewer.repository.api.FlickrDTO
import com.icanmobile.openflickrviewer.repository.api.toFlickerDTO
import com.icanmobile.openflickrviewer.repository.db.FlickrDAO
import com.icanmobile.openflickrviewer.repository.db.toFlickrEntityList
import com.icanmobile.openflickrviewer.ui.viewmodel.state.ViewStateEvent
import com.icanmobile.openflickrviewer.ui.viewmodel.state.ViewState
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

interface FlickrRepository {
    fun searchPhotos(stateEvent: StateEvent, query: String): Flow<DataState<ViewState>>
}

@Singleton
class FlickrRepositoryImpl @Inject constructor(
    private val flickrApi: FlickrApi,
    private val flickrDao: FlickrDAO
): FlickrRepository {

    private var currentPage: AtomicInteger = AtomicInteger()

    override fun searchPhotos(
        stateEvent: StateEvent,
        query: String
    ): Flow<DataState<ViewState>> {
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
                        val flickrDto = runBlocking {
                            flickrDTO.toFlickrEntityList(query)
                                .takeIf { it.isNotEmpty() }
                                ?.run { flickrDao.insertPhotos(this!!) }
                                .let { flickrDao.getPhotos(query) }
                                .run { toFlickerDTO() }
                        }
                        return getDataState(flickrDto)
                    }

                    override fun handleNetworkError(): DataState<ViewState> {
                        val resultObj = runBlocking { flickrDao.getPhotos(query).toFlickerDTO() }
                        return getDataState(resultObj)
                    }

                    private fun getDataState(flickrDto: FlickrDTO): DataState<ViewState> {
                        return DataState.data(
                            data = ViewState(
                                photoListFragmentView = ViewState.PhotoListFragmentView(
                                    photos = flickrDto.toPhotoList()
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