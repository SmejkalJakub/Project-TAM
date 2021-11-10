package com.tama.movieswiper.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProfileDao {
    @Insert
    suspend fun insert(profile: ProfileTable)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param night new value to write
     */
    @Update
    suspend  fun update(profile: ProfileTable)

    /**
     * Selects and returns the row that matches the supplied start time, which is our key.
     *
     * @param key startTimeMilli to match
     */
    @Query("SELECT * from profile_table WHERE profileId = :key")
    suspend fun get(key: Long): ProfileTable?
    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM profile_table")
    suspend fun clear()
}