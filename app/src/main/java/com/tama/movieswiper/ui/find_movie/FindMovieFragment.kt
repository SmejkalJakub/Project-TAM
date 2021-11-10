package com.tama.movieswiper.ui.find_movie

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.tama.movieswiper.R
import com.tama.movieswiper.databinding.FindMovieFragmentBinding
import com.tama.movieswiper.ui.profile.ProfileViewModel

class FindMovieFragment : Fragment() {

    private lateinit var findMovieViewModel: FindMovieViewModel
    private var _binding: FindMovieFragmentBinding  ? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        findMovieViewModel = ViewModelProvider(this).get(FindMovieViewModel::class.java)

        // Get a reference to the binding object and inflate the fragment views.
        _binding = FindMovieFragmentBinding.inflate(
            inflater, container, false)

        val root: View = binding.root

        /*val textView: TextView = binding.textView
        findMovieViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        });*/

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        findMovieViewModel = ViewModelProvider(this).get(FindMovieViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}