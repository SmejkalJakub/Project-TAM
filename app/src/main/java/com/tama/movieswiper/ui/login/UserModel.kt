package com.tama.movieswiper.ui.login

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

data class UserModel(
    var email: String,
    var password: String,
    var auth: FirebaseAuth
)