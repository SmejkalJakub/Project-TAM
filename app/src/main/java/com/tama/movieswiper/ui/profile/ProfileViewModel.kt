package com.tama.movieswiper.ui.profile

import Event
import androidx.lifecycle.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tama.movieswiper.databinding.FragmentProfileBinding
import com.tama.movieswiper.ui.login.UserModel

class ProfileViewModel() : ViewModel() {

    val emailLiveData = MutableLiveData<String>()

    val updateUser = MutableLiveData<Event<UserModel>>()

    fun init(){

        val user = Firebase.auth.currentUser
        emailLiveData.postValue(user?.email)
    }

    fun updateProfileData(binding: FragmentProfileBinding){

        var email = binding.emailProfile.text.toString()
        var password = binding.profilePassword.text.toString()
        var confirmPassword = binding.profileConfirmPassword.text.toString()

        updateUser.postValue(Event(UserModel(email, password, confirmPassword, Firebase.auth)))
    }
}