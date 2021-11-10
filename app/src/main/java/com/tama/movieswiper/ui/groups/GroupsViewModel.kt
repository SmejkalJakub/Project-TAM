package com.tama.movieswiper.ui.groups

import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.tama.movieswiper.R

class GroupsViewModel : ViewModel() {

    fun show_detail(name: String, navController: NavController)
    {
        var test = name
        val bundle = bundleOf("name" to name)
        navController.navigate(R.id.navigation_group_detail, bundle)

    }

    fun switch_to_create_group(navController: NavController)
    {
        navController.navigate(R.id.navigation_create_group)
    }

    fun switch_to_join_group(navController: NavController)
    {
        navController.navigate(R.id.navigation_join_group)
    }
}