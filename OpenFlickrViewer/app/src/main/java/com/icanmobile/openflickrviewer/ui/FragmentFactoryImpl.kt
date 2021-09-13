package com.icanmobile.openflickrviewer.ui

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.icanmobile.openflickrviewer.manager.GlideManager
import com.icanmobile.openflickrviewer.ui.detail.PhotoDetailFragment
import com.icanmobile.openflickrviewer.ui.list.PhotoListFragment
import com.icanmobile.openflickrviewer.ui.photo.PhotoFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Singleton
class FragmentFactoryImpl @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val requestManager: GlideManager
): FragmentFactory(){

    override fun instantiate(classLoader: ClassLoader, className: String) =
        when(className){
            PhotoListFragment::class.java.name -> {
                val fragment = PhotoListFragment(viewModelFactory, requestManager)
                fragment
            }
            PhotoDetailFragment::class.java.name -> {
                val fragment = PhotoDetailFragment(viewModelFactory, requestManager)
                fragment
            }
            PhotoFragment::class.java.name -> {
                val fragment = PhotoFragment(viewModelFactory, requestManager)
                fragment
            }
            else -> {
                error("unknown fragment: $className")
            }
        }
}
