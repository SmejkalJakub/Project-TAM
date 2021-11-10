package com.tama.movieswiper.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_table")
class ProfileTable {
    @PrimaryKey(autoGenerate = false)
    var profileId: Long = 1

    @ColumnInfo(name = "profile_name")
    var name: String = "Profile Name"

    @ColumnInfo(name = "profile_surname")
    var surname: String = "Profile Surname"

    @ColumnInfo(name = "profile_email")
    var email: String = "test_email@somedomain.com"
}