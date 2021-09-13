package com.icanmobile.openflickrviewer.ui

import androidx.fragment.app.FragmentFactory
import com.icanmobile.openflickrviewer.manager.GlideManager
import com.icanmobile.openflickrviewer.ui.detail.PhotoDetailFragment
import com.icanmobile.openflickrviewer.ui.list.PhotoListFragment
import com.icanmobile.openflickrviewer.ui.photo.PhotoFragment
import com.icanmobile.openflickrviewer.ui.viewmodel.FakeMainViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Singleton
class FakeFragmentFactory
@Inject
constructor(
    private val viewModelFactory: FakeMainViewModelFactory,
    private val requestManager: GlideManager
): FragmentFactory(){

    lateinit var uiCommunicationListener: UICommunicationListener

    override fun instantiate(classLoader: ClassLoader, className: String) =
        when(className){
            PhotoListFragment::class.java.name -> {
                val fragment = PhotoListFragment(viewModelFactory, requestManager)
                if(::uiCommunicationListener.isInitialized){
                    fragment.setUICommunicationListener(uiCommunicationListener)
                }
                fragment
            }
            PhotoDetailFragment::class.java.name -> {
                val fragment = PhotoDetailFragment(viewModelFactory, requestManager)
                if(::uiCommunicationListener.isInitialized){
                    fragment.setUICommunicationListener(uiCommunicationListener)
                }
                fragment
            }
            PhotoFragment::class.java.name -> {
                val fragment = PhotoFragment(viewModelFactory, requestManager)
                if(::uiCommunicationListener.isInitialized){
                    fragment.setUICommunicationListener(uiCommunicationListener)
                }
                fragment
            }

            else -> {
                super.instantiate(classLoader, className)
            }
        }
}