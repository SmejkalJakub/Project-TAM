package com.tama.movieswiper.ui.groups

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.tama.movieswiper.R

class CreateGroupViewModel : ViewModel() {
    fun switch_to_group_view(navController: NavController)
    {
        navController.navigate(R.id.navigation_groups)
    }
}