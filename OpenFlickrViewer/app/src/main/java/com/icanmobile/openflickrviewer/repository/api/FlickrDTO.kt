package com.icanmobile.openflickrviewer.repository.api

import com.google.gson.annotations.SerializedName

data class FlickrDTO(
    @SerializedName("photos") val photos: PhotosDTO?
)

data class PhotosDTO(
    @SerializedName("photo") val photoListDTO: List<PhotoDTO>?
)

data class PhotoDTO(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("server") val server: String?,
    @SerializedName("secret") val secret: String?,
    @SerializedName("url") val url: String?
) {

    fun url() = if (url.isNullOrEmpty()) "$BASE_IMAGE_URL$server/${id}_$secret$JPG_EXT" else url

    companion object {
        const val BASE_IMAGE_URL = "https://live.staticflickr.com/"
        const val JPG_EXT = ".jpg"
    }
}
