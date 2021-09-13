package com.icanmobile.openflickrviewer.di

import android.app.Application
import com.icanmobile.openflickrviewer.ui.NavHostFragmentImpl
import com.icanmobile.openflickrviewer.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Singleton
@Component(modules = [
    FragmentModule::class,
    ViewModelModule::class,
    NetworkModule::class,
    DatabaseModule::class,
    RepositoryModule::class,
    GlideModule::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder
        fun build(): AppComponent
    }

    fun inject(mainActivity: MainActivity)

    fun inject(navHostFragmentImpl: NavHostFragmentImpl)
}