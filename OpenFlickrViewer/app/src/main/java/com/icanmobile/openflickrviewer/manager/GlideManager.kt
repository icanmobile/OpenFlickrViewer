package com.icanmobile.openflickrviewer.manager

import android.widget.ImageView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import javax.inject.Inject
import javax.inject.Singleton

interface GlideManager {
    fun setImage(imageUrl: String, imageView: ImageView)
}

@Singleton
class GlideManagerImpl @Inject constructor(
    private val requestManager: RequestManager
): GlideManager{

    override fun setImage(imageUrl: String, imageView: ImageView){
        requestManager.load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }
}