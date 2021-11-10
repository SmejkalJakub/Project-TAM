
package com.tama.movieswiper.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "basic_movies_table")
class BasicMovie {
    @PrimaryKey(autoGenerate = false)
    var movieId: String = ""

    @ColumnInfo(name = "movie_title")
    var movieTitle: String = ""

    @ColumnInfo(name = "movie_rating")
    var rating: Double = -1.0

    @ColumnInfo(name = "movie_image_url")
    var imageURL: String = ""
}