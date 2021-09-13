package com.icanmobile.openflickrviewer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.icanmobile.openflickrviewer.repository.FakeFlickrRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class FakeMainViewModelFactory @Inject constructor(
    private val flickrRepository: FakeFlickrRepositoryImpl
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(flickrRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}