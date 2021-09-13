package com.icanmobile.openflickrviewer.ui.photo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.icanmobile.openflickrviewer.R
import com.icanmobile.openflickrviewer.manager.GlideManager
import com.icanmobile.openflickrviewer.ui.UICommunicationListener
import com.icanmobile.openflickrviewer.ui.detail.PhotoDetailFragment
import com.icanmobile.openflickrviewer.ui.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_photo.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class PhotoFragment(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val requestManager: GlideManager
) : Fragment(R.layout.fragment_photo) {

    lateinit var uiCommunicationListener: UICommunicationListener
    val viewModel: MainViewModel by activityViewModels {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        initUI()
    }

    private fun subscribeObservers(){
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if(viewState != null){
                viewState.photoFragmentView.photoUrl?.let{ url ->
                    setImage(url)
                }
            }
        })
    }

    private fun setImage(imageUrl: String){
        requestManager.setImage(imageUrl, scaling_image_view)
    }

    private fun initUI(){
        uiCommunicationListener.hideStatusBar()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setUICommunicationListener(null)
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