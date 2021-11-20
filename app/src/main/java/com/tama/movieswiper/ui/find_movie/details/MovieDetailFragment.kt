package com.tama.movieswiper.ui.find_movie.details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.tama.movieswiper.MainActivity
import com.tama.movieswiper.R
import com.tama.movieswiper.database.MovieDatabase
import com.tama.movieswiper.databinding.FindMovieFragmentBinding
import com.tama.movieswiper.databinding.MovieDetailFragmentBinding
import com.tama.movieswiper.imdb.MoviesAsynchronousGet
import com.tama.movieswiper.ui.find_movie.FindMovieViewModel
import org.jetbrains.anko.doAsync

class MovieDetailFragment : Fragment() {

    private lateinit var movieDetailViewModel: MovieDetailViewModel
    private var _binding: MovieDetailFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var mDb: MovieDatabase
    private lateinit var navController: NavController
    private lateinit var movieId : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        movieDetailViewModel = ViewModelProvider(this).get(MovieDetailViewModel::class.java)

        // Get a reference to the binding object and inflate the fragment views.
        _binding = MovieDetailFragmentBinding.inflate(
            inflater, container, false)

        movieId = arguments?.getString("id")!!

        movieDetailViewModel.movieName.observe(viewLifecycleOwner, Observer { name ->
            binding.movieDetailTitle.text = name
        })

        movieDetailViewModel.movieDescription.observe(viewLifecycleOwner, Observer { description ->
            binding.movieDetailDescription.text = description
        })

        movieDetailViewModel.originalTitle.observe(viewLifecycleOwner, Observer { org_title ->
            binding.originalTitle.text = org_title
        })

        movieDetailViewModel.moviePoster.observe(viewLifecycleOwner, Observer { poster ->
            binding.movieDetailPicture.setImageBitmap(poster)
        })

        movieDetailViewModel.basicMovieInfo.observe(viewLifecycleOwner, Observer { info ->
            binding.basicInfo.text = info
        })

        movieDetailViewModel.movieGenres.observe(viewLifecycleOwner, Observer { genres ->
            binding.genres.text = genres
        })

        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        movieDetailViewModel.getMovieDetailedInfo(movieId!!)

        binding.backToMovieButton.setOnClickListener(){
            val bundle = bundleOf("id" to movieId)
            navController.navigate(R.id.navigation_find_movie, bundle)
        }
    }

}