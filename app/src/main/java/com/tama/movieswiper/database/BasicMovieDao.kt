package com.tama.movieswiper.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface BasicMovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(movie: BasicMovie)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param night new value to write
     */
    @Update
    fun update(movie: BasicMovie)

    /**
     * Selects and returns the row that matches the supplied start time, which is our key.
     *
     * @param key startTimeMilli to match
     */
    @Query("SELECT * from basic_movies_table WHERE movieId = :key")
    fun get(key: String): BasicMovie?
    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM basic_movies_table")
    fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by start time in descending order.
     */
    @Query("SELECT * FROM basic_movies_table ORDER BY movie_rating DESC")
    fun getAllMovies(): List<BasicMovie>

    @Query("SELECT * FROM basic_movies_table WHERE visited=0")
    fun getNotSeenMovies(): List<BasicMovie>

    /**
     * Selects and returns the latest night.
     */
    @Query("SELECT * FROM basic_movies_table ORDER BY movieId DESC LIMIT 1")
    suspend fun getTopMovie(): BasicMovie?
}