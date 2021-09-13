package com.icanmobile.openflickrviewer.repository.api

import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {

    @GET(SEARCH_API + API_PRESET_VALUES)
    suspend fun searchPhotos(
        @Query("tags") tag: String,
        @Query("page") page: Int = 1
    ): FlickrDTO

    companion object{
        private const val PER_PAGE = 25

        const val BASE_URL = "https://api.flickr.com/services/rest/"
        const val SEARCH_API = "?method=flickr.photos.search"
        const val API_PRESET_VALUES = "&format=json&nojsoncallback=1&sort=date-posted-desc&per_page=$PER_PAGE"
    }
}