/**
* Simple database model for the movie. This information will be saved in the InMemoryDatabase for each visited movie
*/

package com.tama.movieswiper.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "basic_movies_table")
data class BasicMovie(
    @PrimaryKey(autoGenerate = false)
    var movieId: String = "",

    @ColumnInfo(name = "imdb_id")
    var imdbId: String = "",

    @ColumnInfo(name = "movie_title")
    var movieTitle: String = "",

    @ColumnInfo(name = "movie_description")
    var movieDescription: String = "",

    @ColumnInfo(name = "movie_runtime")
    var movieRuntime: Int = 0,

    @ColumnInfo(name = "genres")
    var movieGenres: String = "",

    @ColumnInfo(name = "movie_rating")
    var rating: Double = -1.0,

    @ColumnInfo(name = "movie_image_url")
    var imageURL: String = "",

    @ColumnInfo(name = "visited")
    var visited: Int = 0,

    @ColumnInfo(name = "liked")
    var liked: Int = 0
)