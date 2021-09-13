package com.icanmobile.openflickrviewer.util

import com.icanmobile.openflickrviewer.util.Constants.UNKNOWN_ERROR

abstract class ApiResponseHandler <ViewState, Data>(
    response: ApiResult<Data?>,
    stateEvent: StateEvent
) {
    val result: DataState<ViewState> = when(response){
        is ApiResult.GenericError -> {
            DataState.error(
                errorMessage = stateEvent.errorInfo()
                    + "\n\nReason: " + response.errorMessage,
                stateEvent = stateEvent
            )
        }
        is ApiResult.NetworkError -> {
            handleNetworkError()
        }
        is ApiResult.Success -> {
            if(response.value == null){
                DataState.error(
                    errorMessage = stateEvent.errorInfo()
                        + "\n\nReason: " + UNKNOWN_ERROR,
                    stateEvent = stateEvent
                )
            }
            else{
                handleSuccess(resultObj = response.value)
            }
        }
    }

    abstract fun handleSuccess(resultObj: Data): DataState<ViewState>
    abstract fun handleNetworkError(): DataState<ViewState>
}