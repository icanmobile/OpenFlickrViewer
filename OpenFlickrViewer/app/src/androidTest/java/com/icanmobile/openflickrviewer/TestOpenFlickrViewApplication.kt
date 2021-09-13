package com.icanmobile.openflickrviewer


import com.icanmobile.openflickrviewer.di.DaggerTestAppComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@UseExperimental(InternalCoroutinesApi::class)
class TestOpenFlickrViewApplication : OpenFlickrViewerApplication() {

    override fun initAppComponent() {
        appComponent = DaggerTestAppComponent.builder()
            .application(this)
            .build()
    }
}