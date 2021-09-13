package com.icanmobile.openflickrviewer.repository

import com.icanmobile.openflickrviewer.repository.api.FlickrApi
import com.icanmobile.openflickrviewer.repository.api.FlickrDTO
import com.icanmobile.openflickrviewer.repository.api.PhotoDTO
import com.icanmobile.openflickrviewer.repository.api.PhotosDTO
import com.icanmobile.openflickrviewer.repository.db.FlickrDAO
import com.icanmobile.openflickrviewer.repository.db.toFlickrEntityList
import com.icanmobile.openflickrviewer.ui.viewmodel.state.ViewStateEvent
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class FlickrRepositoryImplTest {

    @MockK lateinit var flickrApi: FlickrApi
    @MockK lateinit var flickrDao: FlickrDAO
    private lateinit var flickrRepository: FlickrRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        flickrRepository = FlickrRepositoryImpl(flickrApi, flickrDao)
    }

    @Test
    fun searchPhotos_withValidPageNumber_returnDataSetSuccessful() = runBlocking {
        coEvery { flickrApi.searchPhotos(any(), any()) } returns getDefaultFlickrDTO()
        coEvery { flickrDao.insertPhotos(any()) } returns longArrayOf(1)
        coEvery { flickrDao.getPhotos(any()) } returns getDefaultFlickrDTO().toFlickrEntityList("dog")

        val firstItem = flickrRepository.searchPhotos(
            stateEvent = ViewStateEvent.SearchNewPhotosEvent("dog"),
            query = "dog"
        ).first()

        assertEquals(firstItem?.data?.photoListFragmentView?.photos?.get(1)?.id, "2")
    }

    private fun getDefaultFlickrDTO() = FlickrDTO(
        PhotosDTO(
            photoListDTO = listOf(
                PhotoDTO(
                    id = "1",
                    title = "title1",
                    server = "server1",
                    secret = "secret1",
                    url = "url1"
                ),
                PhotoDTO(
                    id = "2",
                    title = "title2",
                    server = "server2",
                    secret = "secret2",
                    url = "url2"
                ),
                PhotoDTO(
                    id = "3",
                    title = "title3",
                    server = "server3",
                    secret = "secret3",
                    url = "url3"
                )
            )
        )
    )
}