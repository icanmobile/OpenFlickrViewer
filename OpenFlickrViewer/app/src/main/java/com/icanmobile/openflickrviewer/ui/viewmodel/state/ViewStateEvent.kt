package com.icanmobile.openflickrviewer.ui.viewmodel.state

import com.icanmobile.openflickrviewer.util.StateEvent

sealed class ViewStateEvent: StateEvent {

    object SearchMorePhotosEvent : ViewStateEvent() {
        override fun errorInfo(): String {
            return "Unable to retrieve more photos"
        }

        override fun toString(): String {
            return "SearchMorePhotosEvent"
        }
    }

    data class SearchNewPhotosEvent(
        val query: String
    ): ViewStateEvent() {
        override fun errorInfo(): String {
            return "Unable to retrieve new photos using $query"
        }

        override fun toString(): String {
            return "SearchNewPhotosEvent"
        }
    }
}