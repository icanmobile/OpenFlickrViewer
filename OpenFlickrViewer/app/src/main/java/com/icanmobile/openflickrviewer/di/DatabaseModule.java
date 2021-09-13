package com.icanmobile.openflickrviewer.di;

import android.app.Application;

import com.icanmobile.openflickrviewer.repository.db.FlickrDAO;
import com.icanmobile.openflickrviewer.repository.db.FlickrDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {
    @Singleton
    @Provides
    public FlickrDatabase provideFlickrDatabase(Application application) {
        return FlickrDatabase.Companion.getInstance(application.getApplicationContext());
    }

    @Singleton
    @Provides
    public FlickrDAO provideFlickrDAO(FlickrDatabase flickrDatabase)  {
        return flickrDatabase.flickrDao();
    }
}
