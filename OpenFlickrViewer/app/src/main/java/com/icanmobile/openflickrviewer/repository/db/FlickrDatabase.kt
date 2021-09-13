package com.icanmobile.openflickrviewer.repository.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FlickrEntity::class],
    version = 1
)
abstract class FlickrDatabase : RoomDatabase() {

    abstract fun flickrDao(): FlickrDAO

    companion object {
        private const val DATABASE_NAME = "OpenFlickrViewer.db"

        @Volatile
        private var INSTANCE: FlickrDatabase? = null

        fun getInstance(appContext: Context): FlickrDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(appContext).also { INSTANCE = it }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext,
                FlickrDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}