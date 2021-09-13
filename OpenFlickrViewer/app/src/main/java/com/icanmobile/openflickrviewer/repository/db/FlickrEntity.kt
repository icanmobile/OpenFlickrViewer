package com.icanmobile.openflickrviewer.repository.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = FlickrEntity.TABLE_NAME,
    indices = [Index(value = ["flickrId", "secret"], unique = true)]
)
data class FlickrEntity(
    @ColumnInfo(name = "query") val query: String?,
    @ColumnInfo(name = "flickrId") val flickrId: String?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "server") val server: String?,
    @ColumnInfo(name = "secret") val secret: String?,
    @ColumnInfo(name = "url") val url: String?
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Int = 0

    companion object {
        /** The name of the table.  */
        const val TABLE_NAME = "flicker_table"
    }
}
