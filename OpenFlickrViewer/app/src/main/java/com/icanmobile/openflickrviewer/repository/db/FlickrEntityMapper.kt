package com.icanmobile.openflickrviewer.repository.db

import com.icanmobile.openflickrviewer.repository.api.FlickrDTO

fun FlickrDTO.toFlickrEntityList(query: String): List<FlickrEntity> {
    return this.photos?.photoListDTO?.map {
        FlickrEntity(
            query = query,
            flickrId = it.id.orEmpty(),
            title = it.title.orEmpty(),
            server = it.server.orEmpty(),
            secret = it.secret.orEmpty(),
            url = it.url()
        )
    } ?: emptyList()
}
