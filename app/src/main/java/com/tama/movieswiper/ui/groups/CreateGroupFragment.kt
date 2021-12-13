package com.tama.movieswiper.ui.groups

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.tama.movieswiper.MainActivity
import com.tama.movieswiper.R
import com.tama.movieswiper.databinding.CreateGroupFragmentBinding
import com.tama.movieswiper.databinding.GroupsFragmentBinding

class CreateGroupFragment : Fragment() {

    private lateinit var createGroupViewModel: CreateGroupViewModel
    private var _binding: CreateGroupFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createGroupViewModel = ViewModelProvider(this).get(CreateGroupViewModel::class.java)

        // Get a reference to the binding object and inflate the fragment views.
        _binding = CreateGroupFragmentBinding.inflate(
            inflater, container, false)

        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.backFromCreateButton.setOnClickListener { createGroupViewModel.switch_to_group_view(navController) }
        binding.createButton.setOnClickListener { (activity as MainActivity).create_group(binding.createGroupNameField.text.toString()) }

        binding.createGroupNameField.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                binding.createGroupNameField.setBackgroundResource(R.drawable.rounded_edittext)
            } else {
                binding.createGroupNameField.setBackgroundResource(R.drawable.rounded_edittext_focused)
            }
        }
    }

}