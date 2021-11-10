package com.tama.movieswiper.ui.profile

import Event
import androidx.databinding.Observable
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tama.movieswiper.database.ProfileDao
import com.tama.movieswiper.databinding.FragmentProfileBinding
import com.tama.movieswiper.ui.login.UserModel
import kotlinx.coroutines.launch

class ProfileViewModel() : ViewModel() {

    val emailLiveData = MutableLiveData<String>()

    val updateUser = MutableLiveData<Event<UserModel>>()


    fun init(){

        val user = Firebase.auth.currentUser
        emailLiveData.postValue(user?.email)
    }

    fun updateProfileData(binding: FragmentProfileBinding){

        var email = binding.profileEmail.text.toString()
        var password = binding.profilePassword.text.toString()

        updateUser.postValue(Event(UserModel(email, password, Firebase.auth)))
    }
}