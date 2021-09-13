package com.icanmobile.openflickrviewer.ui

import android.app.Activity
import com.afollestad.materialdialogs.MaterialDialog
import com.icanmobile.openflickrviewer.R

fun Activity.displayErrorDialog(
    errorMessage: String?,
    callback: ErrorDialogCallback
): MaterialDialog {
    return MaterialDialog(this)
        .show{
            title(R.string.text_error)
            message(text = errorMessage)
            positiveButton(R.string.text_ok){
                callback.clearError()
                dismiss()
            }
            cancelOnTouchOutside(false)
        }
}

interface ErrorDialogCallback{
    fun clearError()
}
