package com.tama.movieswiper.ui.find_movie

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tama.movieswiper.databinding.FindMovieFragmentBinding
import com.tama.movieswiper.imdb.MoviesAsynchronousGet
import com.tama.movieswiper.MainActivity
import com.tama.movieswiper.database.MovieDatabase
import org.jetbrains.anko.doAsync


class FindMovieFragment : Fragment() {

    private lateinit var findMovieViewModel: FindMovieViewModel
    private var _binding: FindMovieFragmentBinding  ? = null

    private val binding get() = _binding!!

    lateinit var moviesAsync: MoviesAsynchronousGet

    var movieIndex: Int = 0

    private lateinit var mDb: MovieDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        findMovieViewModel = ViewModelProvider(this).get(FindMovieViewModel::class.java)

        // Get a reference to the binding object and inflate the fragment views.
        _binding = FindMovieFragmentBinding.inflate(
            inflater, container, false)

        val root: View = binding.root

        findMovieViewModel.movieName.observe(viewLifecycleOwner, Observer { name ->
            binding.movieTitle.text = name
        })

        findMovieViewModel.movieDescription.observe(viewLifecycleOwner, Observer { description ->
            binding.movieDescription.text = description
        })

        findMovieViewModel.movieRating.observe(viewLifecycleOwner, Observer { rating ->
            binding.movieRating.text = rating
        })

        findMovieViewModel.moviePoster.observe(viewLifecycleOwner, Observer { poster ->
            binding.moviePicture.setImageBitmap(poster)
        })

        findMovieViewModel.movieRuntime.observe(viewLifecycleOwner, Observer { runtime ->
            binding.movieRuntime.text = runtime
        })

        moviesAsync = MoviesAsynchronousGet(requireActivity().application)

        mDb = MovieDatabase.getInstance(requireActivity().application)!!

        var moviesInDb = false
        doAsync {
            // Get the student list from database
            val list = mDb.basicMovieDao().getNotSeenMovies()

            if(!list.isEmpty())
            {
                findMovieViewModel.changeMovie(list.random())
            }
            else
            {
                moviesAsync.getTopRatedMovies(findMovieViewModel)
            }
        }

        (activity as MainActivity).set_find_movie_binding(binding, moviesAsync, findMovieViewModel, movieIndex)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.showMovie.setOnClickListener(){
            moviesAsync.getTopRatedMovies(findMovieViewModel)
        }

        binding.nextButton.setOnClickListener(){
            movieIndex++
            moviesAsync.getMovieDetails(findMovieViewModel, movieIndex)
        }

        binding.prevButton.setOnClickListener(){
            movieIndex--
            moviesAsync.getMovieDetails(findMovieViewModel, movieIndex)
        }
    }

}