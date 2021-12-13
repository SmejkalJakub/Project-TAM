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
import com.tama.movieswiper.databinding.GroupsFragmentBinding
import com.tama.movieswiper.databinding.JoinGroupFragmentBinding

class JoinGroupFragment : Fragment() {

    private lateinit var joinGroupViewModel: JoinGroupViewModel
    private var _binding: JoinGroupFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        joinGroupViewModel = ViewModelProvider(this).get(JoinGroupViewModel::class.java)

        // Get a reference to the binding object and inflate the fragment views.
        _binding = JoinGroupFragmentBinding.inflate(
            inflater, container, false)

        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.backFromJoinButton.setOnClickListener { joinGroupViewModel.switch_to_group_view(navController) }
        binding.joinButton.setOnClickListener { (activity as MainActivity).join_group(binding.groupNameField.text.toString()) }

        binding.groupNameField.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                binding.groupNameField.setBackgroundResource(R.drawable.rounded_edittext)
            } else {
                binding.groupNameField.setBackgroundResource(R.drawable.rounded_edittext_focused)
            }
        }

    }
}