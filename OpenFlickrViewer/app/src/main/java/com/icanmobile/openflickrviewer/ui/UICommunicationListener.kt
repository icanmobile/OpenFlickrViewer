package com.icanmobile.openflickrviewer.ui

interface UICommunicationListener {

    fun displayMainProgressBar(isLoading: Boolean)

    fun showToolbar()

    fun hideToolbar()

    fun showStatusBar()

    fun hideStatusBar()

    fun showMenu()

    fun hideMenu()

    fun expandAppBar()
}