package com.icanmobile.openflickrviewer.model

import com.icanmobile.openflickrviewer.repository.api.FlickrDTO
import com.icanmobile.openflickrviewer.repository.api.PhotoDTO
import com.icanmobile.openflickrviewer.repository.api.PhotosDTO

fun FlickrDTO.toPhotoList(): List<Photo> {
    return this.photos?.toPhotoList() ?: emptyList()
}

private fun PhotosDTO.toPhotoList(): List<Photo> {
    return this.photoListDTO?.toPhotoList() ?: emptyList()
}

private fun List<PhotoDTO>.toPhotoList(): List<Photo> {
    return this.map {
        Photo(
            id = it.id.orEmpty(),
            title = it.title.orEmpty(),
            server = it.server.orEmpty(),
            secret = it.secret.orEmpty(),
            url = it.url()
        )
    }
}