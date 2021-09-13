package com.icanmobile.openflickrviewer.ui.viewmodel.state

import android.os.Parcelable
import com.icanmobile.openflickrviewer.model.Photo
import kotlinx.android.parcel.Parcelize

const val VIEW_STATE_BUNDLE_KEY = "com.icanmobile.openflickrviewer.ui.viewmodel.state.ViewState"

@Parcelize
data class ViewState(
    var activeJobCounter: HashSet<String> = HashSet(),
    var photoListFragmentView: PhotoListFragmentView = PhotoListFragmentView(),
    var photoDetailFragmentView: PhotoDetailFragmentView = PhotoDetailFragmentView(),
    var photoFragmentView: PhotoFragmentView = PhotoFragmentView()
) : Parcelable {
    @Parcelize
    data class PhotoListFragmentView(var photos: List<Photo>? = null) : Parcelable

    @Parcelize
    data class PhotoDetailFragmentView(var selectedPhoto: Photo? = null) : Parcelable

    @Parcelize
    data class PhotoFragmentView(var photoUrl: String? = null) : Parcelable
}
