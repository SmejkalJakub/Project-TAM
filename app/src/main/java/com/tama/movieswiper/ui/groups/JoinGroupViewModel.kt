package com.tama.movieswiper.ui.groups

import Event
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.tama.movieswiper.R

class JoinGroupViewModel : ViewModel() {
    fun switch_to_group_view(navController: NavController)
    {
        navController.navigate(R.id.navigation_groups)
    }
}
