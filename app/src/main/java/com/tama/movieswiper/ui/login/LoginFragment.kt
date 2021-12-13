package com.tama.movieswiper.ui.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.tama.movieswiper.MainActivity
import com.tama.movieswiper.R
import com.tama.movieswiper.databinding.LoginFragmentBinding
import com.tama.movieswiper.ui.profile.ProfileViewModel

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: LoginFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        // Get a reference to the binding object and inflate the fragment views.
        _binding = LoginFragmentBinding.inflate(
            inflater, container, false)

        val root: View = binding.root

        (activity as MainActivity).set_login_observer(loginViewModel)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.signUpButton.setOnClickListener { loginViewModel.switch_to_sign_up(navController) }
        binding.loginButton.setOnClickListener { loginViewModel.login(binding.loginEmail.text.toString(), binding.loginPassword.text.toString())  }

        binding.loginEmail.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                binding.loginEmail.setBackgroundResource(R.drawable.rounded_edittext)
            } else {
                binding.loginEmail.setBackgroundResource(R.drawable.rounded_edittext_focused)
            }
        }

        binding.loginPassword.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                binding.loginPassword.setBackgroundResource(R.drawable.rounded_edittext)
            } else {
                binding.loginPassword.setBackgroundResource(R.drawable.rounded_edittext_focused)
            }
        }
    }
}