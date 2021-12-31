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
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.IOException
import java.io.InputStream
import java.net.URL


class MovieDetailFragment : Fragment() {

    private lateinit var movieDetailViewModel: MovieDetailViewModel
    private var _binding: MovieDetailFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var mDb: MovieDatabase
    private lateinit var navController: NavController
    private lateinit var movieId : String

    private lateinit var watchLink : String

    var fromGroup : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        movieDetailViewModel = ViewModelProvider(this).get(MovieDetailViewModel::class.java)

        // Get a reference to the binding object and inflate the fragment views.
        _binding = MovieDetailFragmentBinding.inflate(
            inflater, container, false)

        movieId = arguments?.getString("id")!!
        fromGroup = arguments?.getBoolean("fromGroup")!!

        /**
        * Observers for all the movie detail data
        */  
        movieDetailViewModel.movieName.observe(viewLifecycleOwner, Observer { name ->
            binding.movieDetailTitle.text = name
        })

        movieDetailViewModel.movieDescription.observe(viewLifecycleOwner, Observer { description ->
            binding.movieDetailDescription.text = description
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

        movieDetailViewModel.watchLink.observe(viewLifecycleOwner, Observer { link ->
            watchLink = link
        })

        binding.moreButton.setOnClickListener(){
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(watchLink))
            startActivity(browserIntent)
        }

        var imageUrl = "https://image.tmdb.org/t/p/w92/q6tl6Ib6X5FT80RMlcDbexIo4St.jpg"

        val w: Int = 92
        val h: Int = 92
        val conf = Bitmap.Config.ARGB_8888 // see other conf types
        var bitmap: Bitmap = Bitmap.createBitmap(w, h, conf)

        var inputStream: InputStream? = null

        try {
            inputStream = URL(imageUrl).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        binding.appletvLogo.setImageBitmap(bitmap)

        imageUrl = "https://image.tmdb.org/t/p/w92/9A1JSVmSxsyaBK4SUFsYVqbAYfW.jpg"
        try {
            inputStream = URL(imageUrl).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        binding.netflixLogo.setImageBitmap(bitmap)

        imageUrl = "https://image.tmdb.org/t/p/w92/3zw07sM5b9FWcB1QXXt3uLpjn9r.jpg"
        try {
            inputStream = URL(imageUrl).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        binding.hboLogo.setImageBitmap(bitmap)

        imageUrl = "https://image.tmdb.org/t/p/w92/68MNrwlkpF7WnmNPXLah69CR5cb.jpg"
        try {
            inputStream = URL(imageUrl).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        binding.amazonLogo.setImageBitmap(bitmap)

        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logosLayout.visibility = View.GONE
        binding.netflixLogo.visibility = View.GONE
        binding.appletvLogo.visibility = View.GONE
        binding.hboLogo.visibility = View.GONE

        navController = Navigation.findNavController(view)
        movieDetailViewModel.getMovieDetailedInfo(movieId!!, binding)

        if(fromGroup)
        {
            binding.backToMovieButton.setOnClickListener(){
                navController.navigate(R.id.navigation_groups)
            }
        }
        else
        {
            binding.backToMovieButton.setOnClickListener(){
                val bundle = bundleOf("id" to movieId)
                navController.navigate(R.id.navigation_find_movie, bundle)
            }
        }
    }
}