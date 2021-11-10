package com.tama.movieswiper.ui.groups

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.tama.movieswiper.MainActivity
import com.tama.movieswiper.R
import com.tama.movieswiper.databinding.GroupsFragmentBinding
import com.tama.movieswiper.ui.profile.ProfileViewModel

class GroupsFragment : Fragment() {

    private lateinit var groupsViewModel: GroupsViewModel
    private var _binding: GroupsFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        groupsViewModel = ViewModelProvider(this).get(GroupsViewModel::class.java)

        // Get a reference to the binding object and inflate the fragment views.
        _binding = GroupsFragmentBinding.inflate(
            inflater, container, false)

        val root: View = binding.root

        (activity as MainActivity).add_button(binding, groupsViewModel)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.createGroupButton.setOnClickListener { groupsViewModel.switch_to_create_group(navController) }
        binding.joinGroupButton.setOnClickListener { groupsViewModel.switch_to_join_group(navController) }
    }

}