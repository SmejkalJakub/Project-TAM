package com.tama.movieswiper.ui.login

import Event
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tama.movieswiper.R

class LoginViewModel : ViewModel() {

    private lateinit var auth: FirebaseAuth

    val loginSuccesful = MutableLiveData<Event<Boolean>>()
    val loginUser = MutableLiveData<Event<UserModel>>()

    private lateinit var navController: NavController

    fun login(loginEmail: String, loginPassword: String)
    {
        auth = Firebase.auth

        val currentUser = auth.currentUser
        if(currentUser != null){
            loginSuccesful.postValue(Event(true))
            return
        }



        loginUser.postValue(Event(UserModel(loginEmail, loginPassword, null, auth)))
    }

    fun switch_to_sign_up(navController: NavController)
    {
        navController.navigate(R.id.navigation_register)
    }


}

