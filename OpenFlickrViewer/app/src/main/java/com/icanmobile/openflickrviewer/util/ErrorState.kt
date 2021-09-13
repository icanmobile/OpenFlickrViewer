package com.icanmobile.openflickrviewer.util

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ErrorState(
    var message: String
) : Parcelable
