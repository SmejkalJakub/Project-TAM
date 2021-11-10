package com.tama.movieswiper.ui.find_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FindMovieViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is find movie Fragment"
    }
    val text: LiveData<String> = _text
}