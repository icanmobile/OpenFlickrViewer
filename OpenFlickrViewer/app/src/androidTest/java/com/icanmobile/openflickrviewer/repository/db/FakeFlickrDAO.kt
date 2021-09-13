package com.icanmobile.openflickrviewer.repository.db

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.icanmobile.openflickrviewer.repository.api.FlickrDTO
import com.icanmobile.openflickrviewer.util.Constants
import com.icanmobile.openflickrviewer.util.JsonUtil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeFlickrDAO @Inject constructor(
    private val jsonUtil: JsonUtil
): FlickrDAO {

    var flickrJsonFileName: String = Constants.FLICKR_DATA_FILENAME

    override suspend fun insertPhoto(flickrEntity: FlickrEntity): Long {
        return 1
    }

    override suspend fun insertPhotos(flickrEntities: List<FlickrEntity>): LongArray {
        return longArrayOf(flickrEntities.size.toLong())
    }

    override suspend fun getPhotos(query: String): List<FlickrEntity> {
        val rawJson = jsonUtil.readJSONFromAsset(flickrJsonFileName)
        val flickrDTO = Gson().fromJson<FlickrDTO>(
            rawJson,
            object : TypeToken<FlickrDTO>() {}.type
        )
        return flickrDTO.toFlickrEntityList(query)
    }

    override suspend fun deletePhotos(query: String): Int {
        return 0
    }

    override suspend fun deleteAllPhotos() {
        return
    }

}