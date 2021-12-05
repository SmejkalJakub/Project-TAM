package com.tama.movieswiper.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tama.movieswiper.MainActivity
import com.tama.movieswiper.R
import com.tama.movieswiper.databinding.FragmentProfileBinding
import android.widget.Toast



class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        // Get a reference to the binding object and inflate the fragment views.
        _binding = FragmentProfileBinding.inflate(
            inflater, container, false)

        val root: View = binding.root
        (activity as MainActivity).set_profile_observer(profileViewModel)

        profileViewModel.init()
        setFragmentTextObservers()
        setupClickListeners()

        return root
    }

    private fun setupClickListeners() {
        binding.updateButton.setOnClickListener { profileViewModel.updateProfileData(binding) }
        binding.logOutButton.setOnClickListener {
            Firebase.auth.signOut()
            navController.navigate(R.id.navigation_login)
            (activity as MainActivity).show_navView(false)
        }
    }

    private fun setFragmentTextObservers() {
        profileViewModel.emailLiveData.observe(viewLifecycleOwner, Observer { updatedText ->
            binding.emailProfile.setText(updatedText)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.profilePassword.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                Toast.makeText(activity as MainActivity, "focus loosed", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(activity as MainActivity, "focused", Toast.LENGTH_LONG).show()
            }
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}