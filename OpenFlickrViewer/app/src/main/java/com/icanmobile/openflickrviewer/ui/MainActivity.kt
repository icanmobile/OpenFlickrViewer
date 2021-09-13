package com.icanmobile.openflickrviewer.ui

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.appbar.AppBarLayout
import com.icanmobile.openflickrviewer.OpenFlickrViewerApplication
import com.icanmobile.openflickrviewer.R
import com.icanmobile.openflickrviewer.provider.SuggestionProvider
import com.icanmobile.openflickrviewer.ui.viewmodel.*
import com.icanmobile.openflickrviewer.ui.viewmodel.state.VIEW_STATE_BUNDLE_KEY
import com.icanmobile.openflickrviewer.ui.viewmodel.state.ViewState
import com.icanmobile.openflickrviewer.ui.viewmodel.state.ViewStateEvent.SearchNewPhotosEvent
import com.icanmobile.openflickrviewer.util.ERROR_STACK_BUNDLE_KEY
import com.icanmobile.openflickrviewer.util.ErrorStack
import com.icanmobile.openflickrviewer.util.ErrorState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MainActivity : AppCompatActivity(),
    UICommunicationListener {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var suggestions: SearchRecentSuggestions
    private lateinit var searchView: SearchView
    private val dialogs: HashMap<String, MaterialDialog> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as OpenFlickrViewerApplication).appComponent
            .inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar()
        subscribeObservers()
        restoreInstanceState(savedInstanceState)

        // get the search query intents
        handleSearchIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        // get the search query intents
        intent?.let { handleSearchIntent(it) }
    }

    private fun setupActionBar() {
        tool_bar.inflateMenu(R.menu.menu_flickr_viewer)
        tool_bar.title = getString(R.string.app_name)
        setupSearchViewOnActionBar(tool_bar.menu)
        tool_bar.setupWithNavController(
            findNavController(R.id.nav_host_fragment)
        )
    }

    private fun setupSearchViewOnActionBar(menu: Menu) {
        // associate searchable configuration with the search view
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        // search recent suggestion
        suggestions = SearchRecentSuggestions(
            this,
            SuggestionProvider.AUTHORITY,
            SuggestionProvider.MODE
        )
    }

    /**
     * This method handles the intents from the search manager and requests a list of restaurants
     * related to the query included in the intent. Recent query suggestions are simply saved searches.
     * When the user selects one of the suggestions, your searchable activity receives a ACTION_SEARCH intent
     * with the suggestion as the search query, which your searchable activity already handles.
     *
     * @param intent the intent from the search manager
     */
    private fun handleSearchIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                suggestions.saveRecentQuery(query, null)
                viewModel.setStateEvent(SearchNewPhotosEvent(query))
            }
        }
    }

    private fun subscribeObservers(){
        viewModel.viewState.observe(this, Observer { viewState ->
            viewState?.let {
                displayMainProgressBar(viewModel.areAnyJobsActive())
            }
        })

        viewModel.errorState.observe(this, Observer { errorState ->
            errorState?.let {
                displayErrorMessage(errorState)
            }
        })
    }

    private fun displayErrorMessage(errorState: ErrorState) {
        if(!dialogs.containsKey(errorState.message)){
            dialogs[errorState.message] = displayErrorDialog(
                errorState.message,
                object: ErrorDialogCallback {
                    override fun clearError() {
                        viewModel.clearError(0)
                    }
                })
        }
    }

    private fun restoreInstanceState(savedInstanceState: Bundle?){
        savedInstanceState?.let { inState ->
            (inState[VIEW_STATE_BUNDLE_KEY] as ViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
            (inState[ERROR_STACK_BUNDLE_KEY] as ArrayList<ErrorState>?)?.let { stack ->
                val errorStack = ErrorStack()
                errorStack.addAll(stack)
                viewModel.setErrorStack(errorStack)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.clearActiveJobCounter()
        outState.putParcelable(
            VIEW_STATE_BUNDLE_KEY,
            viewModel.getCurrentViewStateOrNew()
        )
        outState.putParcelableArrayList(
            ERROR_STACK_BUNDLE_KEY,
            viewModel.errorStack
        )
        super.onSaveInstanceState(outState)
    }


    //region - UICommunicationListener interface methods
    override fun displayMainProgressBar(isLoading: Boolean){
        if(isLoading){
            main_progress_bar.visibility = View.VISIBLE
        }
        else{
            main_progress_bar.visibility = View.GONE
        }
    }

    override fun showToolbar() {
        tool_bar.visibility = View.VISIBLE
    }

    override fun hideToolbar() {
        tool_bar.visibility = View.GONE
    }

    override fun showStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        showToolbar()
    }

    override fun hideStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        hideToolbar()
    }

    override fun showMenu() {
        tool_bar.menu.findItem(R.id.search).isVisible = true
    }
    override fun hideMenu() {
        tool_bar.menu.findItem(R.id.search).isVisible = false
    }

    override fun expandAppBar() {
        findViewById<AppBarLayout>(R.id.app_bar).setExpanded(true)
    }
    //endregion - UICommunicationListener interface methods


    override fun onDestroy() {
        cleanUpOnDestroy()
        super.onDestroy()
    }

    private fun cleanUpOnDestroy(){
        for(dialog in dialogs){
            dialog.value.dismiss()
        }
    }
}