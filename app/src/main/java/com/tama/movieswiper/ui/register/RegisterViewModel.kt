package com.tama.movieswiper.ui.register

import Event
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import com.tama.movieswiper.R
import com.tama.movieswiper.ui.login.UserModel

class RegisterViewModel : ViewModel() {

    private lateinit var auth: FirebaseAuth

    val registerUser = MutableLiveData<Event<UserModel>>()


    fun register(registerEmail: String, registerPassword: String, confirmationPassword: String)
    {
        auth = Firebase.auth

        registerUser.postValue(Event(UserModel(registerEmail, registerPassword, confirmationPassword, auth)))
    }

    fun return_to_login(navController: NavController)
    {
        navController.navigate(R.id.navigation_login)
    }


}