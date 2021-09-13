package com.icanmobile.openflickrviewer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icanmobile.openflickrviewer.repository.FlickrRepository
import com.icanmobile.openflickrviewer.ui.viewmodel.state.ViewState
import com.icanmobile.openflickrviewer.ui.viewmodel.state.ViewStateEvent
import com.icanmobile.openflickrviewer.ui.viewmodel.state.ViewStateEvent.SearchMorePhotosEvent
import com.icanmobile.openflickrviewer.ui.viewmodel.state.ViewStateEvent.SearchNewPhotosEvent
import com.icanmobile.openflickrviewer.util.DataState
import com.icanmobile.openflickrviewer.util.ErrorStack
import com.icanmobile.openflickrviewer.util.ErrorState
import com.icanmobile.openflickrviewer.util.StateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class MainViewModel @Inject constructor(
    private val flickrRepository: FlickrRepository
): ViewModel() {

    private val dataChannel = ConflatedBroadcastChannel<DataState<ViewState>>()

    private val _viewState: MutableLiveData<ViewState> = MutableLiveData()
    val viewState: LiveData<ViewState>
        get() = _viewState

    val errorStack = ErrorStack()
    val errorState: LiveData<ErrorState> = errorStack.errorState

    private var currentQuery: String = ""

    init {
        setupChannel()
    }

    private fun setupChannel(){
        dataChannel
            .asFlow()
            .onEach{ dataState ->
                dataState.data?.let { data ->
                    handleNewData(dataState.stateEvent, data)
                }
                dataState.error?.let { error ->
                    handleNewError(dataState.stateEvent, error)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun handleNewData(stateEvent: StateEvent, data: ViewState){
        data.photoListFragmentView.photos?.let { photos ->
            setPhotoList(photos)
        }
        removeJobFromCounter(stateEvent.toString())
    }

    private fun handleNewError(stateEvent: StateEvent, error: ErrorState) {
        appendErrorState(error)
        removeJobFromCounter(stateEvent.toString())
    }

    fun setViewState(viewState: ViewState){
        _viewState.value = viewState
    }

    fun setStateEvent(stateEvent: ViewStateEvent) {
        when (stateEvent) {
            is SearchNewPhotosEvent -> {
                currentQuery = stateEvent.query
                launchJob(
                    stateEvent,
                    flickrRepository.searchPhotos(stateEvent, currentQuery)
                )
            }
            is SearchMorePhotosEvent -> {
                if (currentQuery.isNotEmpty()) {
                    launchJob(
                        stateEvent,
                        flickrRepository.searchPhotos(stateEvent, currentQuery)
                    )
                }
            }
        }
    }

    private fun launchJob(stateEvent: StateEvent, jobFunction: Flow<DataState<ViewState>>){
        if(!isJobAlreadyActive(stateEvent.toString())){
            addJobToCounter(stateEvent.toString())
            jobFunction
                .onEach { dataState ->
                    offerToDataChannel(dataState)
                }
                .launchIn(viewModelScope)
        }
    }

    private fun offerToDataChannel(dataState: DataState<ViewState>){
        if(!dataChannel.isClosedForSend){
            dataChannel.offer(dataState)
        }
    }
}