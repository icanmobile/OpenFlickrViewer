package com.icanmobile.openflickrviewer.repository.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.icanmobile.openflickrviewer.util.Constants
import com.icanmobile.openflickrviewer.util.JsonUtil
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeFlickrApi @Inject constructor(
    private val jsonUtil: JsonUtil
): FlickrApi {

    var flickrJsonFileName: String = Constants.FLICKR_DATA_FILENAME
    var networkDelay: Long = 0L

    override suspend fun searchPhotos(tag: String, page: Int): FlickrDTO {
        val rawJson = jsonUtil.readJSONFromAsset(flickrJsonFileName)
        val flickrDTO = Gson().fromJson<FlickrDTO>(
            rawJson,
            object : TypeToken<FlickrDTO>() {}.type
        )
        delay(networkDelay)
        return flickrDTO
    }
}