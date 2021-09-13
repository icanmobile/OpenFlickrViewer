package com.icanmobile.openflickrviewer.ui.detail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.icanmobile.openflickrviewer.R
import com.icanmobile.openflickrviewer.manager.GlideManager
import com.icanmobile.openflickrviewer.model.Photo
import com.icanmobile.openflickrviewer.ui.UICommunicationListener
import com.icanmobile.openflickrviewer.ui.viewmodel.MainViewModel
import com.icanmobile.openflickrviewer.ui.viewmodel.clickPhotoDetail
import kotlinx.android.synthetic.main.fragment_photo_detail.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class PhotoDetailFragment(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val requestManager: GlideManager
) : Fragment(R.layout.fragment_photo_detail) {

    private lateinit var uiCommunicationListener: UICommunicationListener
    val viewModel: MainViewModel by activityViewModels { viewModelFactory }
    private var photoUrl: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        initUI()
    }

    private fun initUI(){
        uiCommunicationListener.hideMenu()
        uiCommunicationListener.showStatusBar()
        uiCommunicationListener.expandAppBar()

        photo_image.setOnClickListener {
            photoUrl?.let {
                viewModel.clickPhotoDetail(it)
                findNavController().navigate(R.id.action_detailFragment_to_photoFragment)
            }
        }
    }

    private fun subscribeObservers(){
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState?.let {
                it.photoDetailFragmentView.selectedPhoto?.let{ selectedPhoto ->
                    setPhotoToView(selectedPhoto)
                }
            }
        })
    }

    private fun setPhotoToView(photo: Photo){
        requestManager.setImage(photo.url, photo_image)
        photo_title.text = photo.title
        photo_id.text = "ID: ${photo.id}"
        photo_url.text = "URL: ${photo.url}"
        photoUrl = photo.url
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setUICommunicationListener()
    }

    fun setUICommunicationListener(uiICommunicationListener: UICommunicationListener? = null){
        if(uiICommunicationListener != null){
            this.uiCommunicationListener = uiICommunicationListener
        }
        else{
            try {
                uiCommunicationListener = (context as UICommunicationListener)
            }catch (e: Exception){
                Log.e(PhotoDetailFragment::class.simpleName, "$context must implement UICommunicationListener")
            }
        }
    }
}