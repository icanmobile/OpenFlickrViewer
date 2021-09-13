package com.icanmobile.openflickrviewer.ui.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.icanmobile.openflickrviewer.R
import com.icanmobile.openflickrviewer.manager.GlideManager
import com.icanmobile.openflickrviewer.model.Photo
import com.icanmobile.openflickrviewer.ui.UICommunicationListener
import com.icanmobile.openflickrviewer.ui.detail.PhotoDetailFragment
import com.icanmobile.openflickrviewer.ui.viewmodel.*
import com.icanmobile.openflickrviewer.ui.viewmodel.state.ViewState
import com.icanmobile.openflickrviewer.ui.viewmodel.state.ViewStateEvent
import com.icanmobile.openflickrviewer.ui.viewmodel.state.ViewStateEvent.SearchMorePhotosEvent
import kotlinx.android.synthetic.main.fragment_photo_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class PhotoListFragment(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val requestManager: GlideManager
) : Fragment(R.layout.fragment_photo_list), PhotoListAdapter.Interaction {

    private lateinit var uiCommunicationListener: UICommunicationListener
    val viewModel: MainViewModel by activityViewModels {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiCommunicationListener.showMenu()
        initRecyclerView()
        subscribeObservers()
    }

    private fun initRecyclerView(){
        recycler_view.apply {
            layoutManager = GridLayoutManager(this@PhotoListFragment.context, 2)
            addItemDecoration(GridItemDecoration(16))
            itemAnimator = DefaultItemAnimator()
            adapter = PhotoListAdapter(requestManager, this@PhotoListFragment)

            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1)) {
                        // search more photos
                        viewModel.setStateEvent(SearchMorePhotosEvent)
                    }
                }
            })
        }
    }

    private fun subscribeObservers(){
        viewModel.viewState.observe(viewLifecycleOwner, observer)
    }

    private val observer: Observer<ViewState> = Observer { viewState ->
        viewState?.photoListFragmentView?.let{ view ->
            view.photos?.let { photos ->
                (recycler_view.adapter as PhotoListAdapter).apply {
                    submitList(photos)
                }
                displayTheresNothingHereTextView((photos.isNotEmpty()))
            }
        }
    }

    private fun displayTheresNothingHereTextView(isDataAvailable: Boolean){
        if(isDataAvailable){
            no_data_textview.visibility = View.GONE
        }
        else{
            no_data_textview.visibility = View.VISIBLE
        }
    }

    override fun onItemSelected(position: Int, item: Photo) {
        removeViewStateObserver()
        viewModel.selectPhotoListItem(photo = item)
        findNavController().navigate(R.id.action_listFragment_to_detailFragment)
    }

    private fun removeViewStateObserver(){
        viewModel.viewState.removeObserver(observer)
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

    @VisibleForTesting
    fun newSearch(query: String) {
        viewModel.setStateEvent(ViewStateEvent.SearchNewPhotosEvent(query))
    }
}