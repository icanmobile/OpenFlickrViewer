package com.icanmobile.openflickrviewer.ui

import android.content.Context
import androidx.fragment.app.FragmentFactory
import androidx.navigation.fragment.NavHostFragment
import com.icanmobile.openflickrviewer.OpenFlickrViewerApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class NavHostFragmentImpl : NavHostFragment(){
    @Inject lateinit var fragmentFactory: FragmentFactory

    override fun onAttach(context: Context) {
        (activity?.application as OpenFlickrViewerApplication).appComponent
            .inject(this)
        childFragmentManager.fragmentFactory = fragmentFactory
        super.onAttach(context)
    }
}
