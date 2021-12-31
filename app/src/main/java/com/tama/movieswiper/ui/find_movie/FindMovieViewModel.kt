package com.tama.movieswiper.ui.find_movie

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.room.Room
import com.haroldadmin.cnradapter.NetworkResponse
import com.tama.movieswiper.R
import com.tama.movieswiper.database.BasicMovie
import com.tama.movieswiper.database.MovieDatabase
import de.vkay.api.tmdb.TMDb
import de.vkay.api.tmdb.models.TmdbMovie
import kotlinx.coroutines.*
import org.jetbrains.anko.doAsync
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.URL
import kotlin.random.Random

class FindMovieViewModel(application: Application) : AndroidViewModel(application) {

    val movieName = MutableLiveData<String>()
    val movieDescription = MutableLiveData<String>()
    val movieRating = MutableLiveData<String>()
    val moviePoster = MutableLiveData<Bitmap>()
    val movieRuntime = MutableLiveData<String>()

    val movieGenres = MutableLiveData<List<String>>()

    val currentMovie = MutableLiveData<BasicMovie>()

    private lateinit var mDb:MovieDatabase

    var loaded: Boolean = false

    /**
    * Save movie info to the in memory database so it will not be shown again
    */
    fun saveMovieToDb(movieId: Int, movieData: TmdbMovie)
    {
        mDb = MovieDatabase.getInstance(getApplication<Application>().applicationContext)!!

        val title = movieData.title

        var genres = ""
        for(genre in movieData.genres)
        {
            genres += "${genre.name}, "
        }
        genres.removeSuffix(", ")

        val imageURL = movieData.poster?.filePath

        val descriptionText = movieData.overview

        val ratingScore = movieData.voteAverage

        var runtimeInMinutes = movieData.runtime

        var movie = BasicMovie()

        try {
            movie.movieId = movieId.toString()
            movie.imdbId = movieData.imdbId.toString()
            movie.movieDescription = descriptionText
            movie.movieRuntime = runtimeInMinutes
            movie.movieTitle = title
            movie.rating = ratingScore
            movie.imageURL = "https://image.tmdb.org/t/p/w342/$imageURL"
            movie.movieGenres = genres
            movie.visited = 0
            movie.liked = 0
        }
        catch (e: IOException)
        {
            println("EXEPTION")
            return
        }

        mDb.runInTransaction(java.lang.Runnable {
            mDb.basicMovieDao().insert(movie)
        })
    }

    /**
    * Change to a new not visited movie
    */
    fun changeMovie(movie: BasicMovie)
    {
        val w: Int = 150
        val h: Int = 150
        val conf = Bitmap.Config.ARGB_8888 // see other conf types
        var bitmap: Bitmap = Bitmap.createBitmap(w, h, conf)

        var inputStream: InputStream? = null

        try {
            inputStream = URL(movie.imageURL).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        movieName.postValue(movie.movieTitle)
        movieDescription.postValue(movie.movieDescription)
        movieRating.postValue("Rating: ${movie.rating}/10 ★")
        moviePoster.postValue(bitmap)
        movieRuntime.postValue("Runtime: ${movie.movieRuntime} minutes" )

        var genres = movie.movieGenres.split(", ")

        movieGenres.postValue(genres)

        currentMovie.postValue(movie)
    }

    /**
    * Move to movie detail and show the detailed info
    */
    fun getMovieDetailedInfo(navController: NavController)
    {
        var id = currentMovie.value?.movieId

        val bundle = bundleOf(Pair("id", id), Pair("fromGroup", false))
        navController.navigate(R.id.navigation_movie_detail, bundle)
    }

    /**
    * Select random movie
    */
    fun changeToRandomMovie()
    {
        mDb = MovieDatabase.getInstance(getApplication<Application>().applicationContext)!!

        doAsync {
            // Get the student list from database
            val list = mDb.basicMovieDao().getNotSeenMovies()

            if (!list.isEmpty()) {
                changeMovie(list.random())
            }
        }
    }
}