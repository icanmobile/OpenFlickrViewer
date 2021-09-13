package com.icanmobile.openflickrviewer.repository.api

import com.icanmobile.openflickrviewer.repository.db.FlickrEntity

fun List<FlickrEntity>.toFlickerDTO(): FlickrDTO {
    val photos = this.map {
        PhotoDTO(
            id = it.flickrId,
            title = it.title,
            server = it.server,
            secret = it.secret,
            url = it.url
        )
    }
    return FlickrDTO(PhotosDTO(photos))
}