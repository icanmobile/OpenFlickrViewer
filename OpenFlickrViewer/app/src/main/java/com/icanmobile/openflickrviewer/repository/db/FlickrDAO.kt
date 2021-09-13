package com.icanmobile.openflickrviewer.repository.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FlickrDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(flickrEntity: FlickrEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(flickrEntities: List<FlickrEntity>): LongArray

    @Query("SELECT * FROM " + FlickrEntity.TABLE_NAME + " WHERE query = :query")
    suspend fun getPhotos(query: String): List<FlickrEntity>

    @Query("DELETE FROM " + FlickrEntity.TABLE_NAME + " WHERE query = :query")
    suspend fun deletePhotos(query: String): Int

    @Query("DELETE FROM " + FlickrEntity.TABLE_NAME)
    suspend fun deleteAllPhotos()
}