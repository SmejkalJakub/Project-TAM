package com.tama.movieswiper.ui.register

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
import com.tama.movieswiper.databinding.LoginFragmentBinding
import com.tama.movieswiper.databinding.RegisterFragmentBinding
import com.tama.movieswiper.ui.login.LoginViewModel

class RegisterFragment : Fragment() {

    private lateinit var registerViewModel: RegisterViewModel
    private var _binding: RegisterFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        // Get a reference to the binding object and inflate the fragment views.
        _binding = RegisterFragmentBinding.inflate(
            inflater, container, false)

        val root: View = binding.root
        (activity as MainActivity).set_register_observer(registerViewModel)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.registerButton.setOnClickListener { registerViewModel.register(binding.registerEmail.text.toString(),
            binding.registerPassword.text.toString(), binding.confirmPassword.text.toString()) }
        
        binding.backButton.setOnClickListener { registerViewModel.return_to_login(navController) }
    }
}