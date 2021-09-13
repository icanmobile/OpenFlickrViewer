package com.icanmobile.openflickrviewer.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
    val id: String,
    val title: String,
    val server: String,
    val secret: String,
    val url: String
) : Parcelable