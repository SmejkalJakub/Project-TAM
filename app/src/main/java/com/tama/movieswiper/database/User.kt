/**
* Data class for the user for the database
*/

package com.tama.movieswiper.database

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(val id: String? = null, val email: String? = null) {
}