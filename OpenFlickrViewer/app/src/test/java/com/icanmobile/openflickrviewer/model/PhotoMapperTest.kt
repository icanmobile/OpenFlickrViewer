package com.icanmobile.openflickrviewer.model

import com.icanmobile.openflickrviewer.repository.api.FlickrDTO
import com.icanmobile.openflickrviewer.repository.api.PhotoDTO
import com.icanmobile.openflickrviewer.repository.api.PhotosDTO
import org.junit.Assert.assertEquals
import org.junit.Test

class PhotoMapperTest {

    @Test
    fun toPhotoList_withFlickrDTO_shouldReturnPhotoList() {
        val photo = getDefaultFlickrDTO().toPhotoList()[2]

        assertEquals(photo.id, "3")
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