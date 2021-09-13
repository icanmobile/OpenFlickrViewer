package com.icanmobile.openflickrviewer.ui.viewmodel

import com.icanmobile.openflickrviewer.model.Photo
import com.icanmobile.openflickrviewer.ui.viewmodel.state.ViewState
import com.icanmobile.openflickrviewer.util.ErrorStack
import com.icanmobile.openflickrviewer.util.ErrorState
import com.icanmobile.openflickrviewer.util.EspressoIdlingResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
fun MainViewModel.getCurrentViewStateOrNew(): ViewState {
    return viewState.value?.let {
        it
    } ?: ViewState()
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
fun MainViewModel.setPhotoList(photoList: List<Photo>){
    val update = getCurrentViewStateOrNew()
    update.photoListFragmentView.photos = photoList
    setViewState(update)
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
fun MainViewModel.selectPhotoListItem(photo: Photo){
    val update = getCurrentViewStateOrNew()
    update.photoDetailFragmentView.selectedPhoto = photo
    setViewState(update)
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
fun MainViewModel.clickPhotoDetail(photoUrl: String){
    val update = getCurrentViewStateOrNew()
    update.photoFragmentView.photoUrl = photoUrl
    setViewState(update)
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
fun MainViewModel.removeJobFromCounter(stateEventName: String){
    val update = getCurrentViewStateOrNew()
    update.activeJobCounter.remove(stateEventName)
    setViewState(update)
    EspressoIdlingResource.decrement()
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
fun MainViewModel.isJobAlreadyActive(stateEventName: String): Boolean {
    val viewState = getCurrentViewStateOrNew()
    return viewState.activeJobCounter.contains(stateEventName)
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
fun MainViewModel.addJobToCounter(stateEventName: String){
    val update = getCurrentViewStateOrNew()
    update.activeJobCounter.add(stateEventName)
    setViewState(update)
    EspressoIdlingResource.increment()
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
fun MainViewModel.areAnyJobsActive(): Boolean{
    val viewState = getCurrentViewStateOrNew()
    return viewState.activeJobCounter.size > 0
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
fun MainViewModel.clearActiveJobCounter(){
    val update = getCurrentViewStateOrNew()
    update.activeJobCounter.clear()
    setViewState(update)
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
fun MainViewModel.appendErrorState(errorState: ErrorState){
    errorStack.add(errorState)
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
fun MainViewModel.setErrorStack(errorStack: ErrorStack){
    this.errorStack.addAll(errorStack)
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
fun MainViewModel.clearError(index: Int){
    errorStack.removeAt(index)
}