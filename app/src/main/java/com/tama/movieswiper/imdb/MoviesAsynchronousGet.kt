package com.tama.movieswiper.imdb

import android.app.Application
import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.oAuthProvider
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.haroldadmin.cnradapter.NetworkResponse
import com.tama.movieswiper.database.MovieDatabase
import com.tama.movieswiper.ui.find_movie.FindMovieViewModel
import de.vkay.api.tmdb.Discover
import de.vkay.api.tmdb.TMDb
import de.vkay.api.tmdb.models.TmdbMovie
import de.vkay.api.tmdb.models.TmdbPage
import kotlinx.coroutines.runBlocking
import okhttp3.*
import org.jetbrains.anko.doAsync
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MoviesAsynchronousGet(application: Application) {
    private val client = OkHttpClient()

    var movies = arrayOf<Int>()
    private lateinit var mDb:MovieDatabase

    var loaded : Boolean = false

    private var context: Context? = application

    fun getMovieDetails(viewModel: FindMovieViewModel, movieIndex: Int) {

        var movieId = movies[movieIndex]
        runBlocking {
            TMDb.init("1373e6da8f4f694cc751405fd528bb62")

            when (val response = TMDb.movieService.details(movieId, "en")) {
                // Type: NetworkResponse<TmdbPage<TmdbShow.Slim>, TmdbError.DefaultError>
                is NetworkResponse.Success -> {
                    val foundedMovie: TmdbMovie = response.body
                    viewModel.saveMovieToDb(movieId, foundedMovie)

                }
                is NetworkResponse.ServerError -> {
                    /*val errorBody: TmdbErrorResponse = response.body*/
                    println("ServerError: Message")
                }
                is NetworkResponse.NetworkError -> {
                    val error: IOException = response.error
                    println("NetworkError: Message = ${error.message}")
                }
                is NetworkResponse.UnknownError -> {
                    val error: Throwable = response.error
                    println("UnknownError: Message = ${error.message}")
                }
            }
        }
    }

    fun getTopRatedMovies(viewModel: FindMovieViewModel) {

        var i = 0
        runBlocking {
            TMDb.init("1373e6da8f4f694cc751405fd528bb62")

            when (val response = TMDb.movieService.topRated(languageTag = "en", page = 1, languageCode = "US")) {
                // Type: NetworkResponse<TmdbPage<TmdbShow.Slim>, TmdbError.DefaultError>
                is NetworkResponse.Success -> {
                    val searchPage: TmdbPage<TmdbMovie.Slim> = response.body
                    movies = Array<Int>(searchPage.results.size, { i -> 0 })
                    for(result in searchPage.results)
                    {
                        movies[i] = result.id
                        i++
                    }
                    for(i in 0..(movies.size - 1))
                    {
                        getMovieDetails(viewModel, i)
                    }
                }
                is NetworkResponse.ServerError -> {
                    /*val errorBody: TmdbErrorResponse = response.body*/
                    println("ServerError: Message")
                }
                is NetworkResponse.NetworkError -> {
                    val error: IOException = response.error
                    println("NetworkError: Message = ${error.message}")
                }
                is NetworkResponse.UnknownError -> {
                    val error: Throwable = response.error
                    println("UnknownError: Message = ${error.message}")
                }
            }
        }
    }
    fun loadNewMovies(viewModel: FindMovieViewModel, currentUser: FirebaseUser?) {
        var i = 0

        val database = Firebase.database("https://tama-project-26b9d-default-rtdb.europe-west1.firebasedatabase.app/").reference
        var genres = database.child("users").child(currentUser?.uid.toString()).child("genres")

        var likedGenres = List<Int>(3, init = { i -> 0 })
        var dislikedGenres = List<Int>(3, init = { i -> 0 })

        /*when (currentUser.)
        {
            "Action" -> currentUserPreferences.action += addition
            "Adventure" -> currentUserPreferences.adventure += addition
            "Animation" -> currentUserPreferences.animation += addition
            "Comedy" -> currentUserPreferences.comedy += addition
            "Crime" -> currentUserPreferences.crime += addition
            "Documentary" -> currentUserPreferences.documentary += addition
            "Drama" -> currentUserPreferences.drama += addition
            "Family" -> currentUserPreferences.family += addition
            "Fantasy" -> currentUserPreferences.fantasy += addition
            "History" -> currentUserPreferences.history += addition
            "Horror" -> currentUserPreferences.horror += addition
            "Music" -> currentUserPreferences.music += addition
            "Mystery" -> currentUserPreferences.mystery += addition
            "Romance" -> currentUserPreferences.romance += addition
            "Science Fiction" -> currentUserPreferences.sci_fi += addition
            "TV Movie" -> currentUserPreferences.tv_movie += addition
            "Thriller" -> currentUserPreferences.thriller += addition
            "War" -> currentUserPreferences.war += addition
            "Western" -> currentUserPreferences.western += addition
        }*/

        runBlocking {
            TMDb.init("1373e6da8f4f694cc751405fd528bb62")
            var movieBuilder = Discover.MovieBuilder().sortBy(Discover.SortBy.POPULARITY_DESC).withGenres(likedGenres).withoutGenres(dislikedGenres)

            when (val response = TMDb.discoverService.movie(languageTag = "en", page = 1, options = movieBuilder)) {
                // Type: NetworkResponse<TmdbPage<TmdbShow.Slim>, TmdbError.DefaultError>
                is NetworkResponse.Success -> {
                    val searchPage: TmdbPage<TmdbMovie.Slim> = response.body
                    movies = Array<Int>(searchPage.results.size, { i -> 0 })
                    for(result in searchPage.results)
                    {
                        movies[i] = result.id
                        i++
                    }
                    for(i in 0..(movies.size - 1))
                    {
                        getMovieDetails(viewModel, i)
                    }
                }
                is NetworkResponse.ServerError -> {
                    /*val errorBody: TmdbErrorResponse = response.body*/
                    println("ServerError: Message")
                }
                is NetworkResponse.NetworkError -> {
                    val error: IOException = response.error
                    println("NetworkError: Message = ${error.message}")
                }
                is NetworkResponse.UnknownError -> {
                    val error: Throwable = response.error
                    println("UnknownError: Message = ${error.message}")
                }
            }
        }
    }
}