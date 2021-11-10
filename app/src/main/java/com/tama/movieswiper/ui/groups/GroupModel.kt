package com.tama.movieswiper.ui.groups

import com.google.firebase.database.IgnoreExtraProperties
import com.tama.movieswiper.database.User
import com.tama.movieswiper.ui.login.UserModel

@IgnoreExtraProperties
data class GroupModel
    (
        var name: String? = null,
        var owner: String? = null,
    )
{
}