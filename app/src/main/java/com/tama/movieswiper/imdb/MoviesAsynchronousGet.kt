package com.tama.movieswiper.imdb

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.oAuthProvider
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.haroldadmin.cnradapter.NetworkResponse
import com.tama.movieswiper.R
import com.tama.movieswiper.database.MovieDatabase
import com.tama.movieswiper.database.User
import com.tama.movieswiper.ui.find_movie.FindMovieViewModel
import de.vkay.api.tmdb.Discover
import de.vkay.api.tmdb.TMDb
import de.vkay.api.tmdb.models.TmdbMovie
import de.vkay.api.tmdb.models.TmdbPage
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.*
import org.jetbrains.anko.doAsync
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.lang.Math.random
import java.time.LocalDate

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
            viewModel.changeToRandomMovie()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun loadNewMovies(viewModel: FindMovieViewModel, currentUser: FirebaseUser?) {
        var i = 0

        var likedGenresIndex = 0
        var unlikedGenresIndex = 0

        var likedGenres = MutableList<Int>(2, init = { i -> 0 })
        var dislikedGenres = MutableList<Int>(2, init = { i -> 0 })

        val database = Firebase.database("https://tama-project-26b9d-default-rtdb.europe-west1.firebasedatabase.app/").reference

        var minimum = 110
        var maximum = 0

        var minimumGenre : Int = 0
        var maximumGenre : Int = 0

        runBlocking {
            database.child("users").child(currentUser?.uid.toString()).child("genres").get()
                .addOnSuccessListener {
                    for (genre in it.children) {
                        if(genre.value.toString().toInt() < minimum)
                        {
                            minimum = genre.value.toString().toInt()
                            when (genre.key) {
                                "action" -> minimumGenre = 28
                                "adventure" -> minimumGenre = 12
                                "animation" -> minimumGenre = 16
                                "comedy" -> minimumGenre = 35
                                "crime" -> minimumGenre = 80
                                "documentary" -> minimumGenre = 99
                                "drama" -> minimumGenre = 18
                                "family" -> minimumGenre = 10751
                                "fantasy" -> minimumGenre = 14
                                "history" -> minimumGenre = 36
                                "horror" -> minimumGenre = 27
                                "music" -> minimumGenre = 10402
                                "mystery" -> minimumGenre = 9648
                                "romance" -> minimumGenre = 10749
                                "sci-fi" -> minimumGenre = 878
                                "tv_movie" -> minimumGenre = 10770
                                "thriller" -> minimumGenre = 53
                                "war" -> minimumGenre = 10752
                                "western" -> minimumGenre = 37
                            }
                        }
                        if(genre.value.toString().toInt() > maximum)
                        {
                            maximum = genre.value.toString().toInt()
                            when (genre.key) {
                                "action" -> maximumGenre = 28
                                "adventure" -> maximumGenre = 12
                                "animation" -> maximumGenre = 16
                                "comedy" -> maximumGenre = 35
                                "crime" -> maximumGenre = 80
                                "documentary" -> maximumGenre = 99
                                "drama" -> maximumGenre = 18
                                "family" -> maximumGenre = 10751
                                "fantasy" -> maximumGenre = 14
                                "history" -> maximumGenre = 36
                                "horror" -> maximumGenre = 27
                                "music" -> maximumGenre = 10402
                                "mystery" -> maximumGenre = 9648
                                "romance" -> maximumGenre = 10749
                                "sci-fi" -> maximumGenre = 878
                                "tv_movie" -> maximumGenre = 10770
                                "thriller" -> maximumGenre = 53
                                "war" -> maximumGenre = 10752
                                "western" -> maximumGenre = 37
                            }

                        }

                        if (genre.value.toString().toInt() > 68 && likedGenresIndex < 2) {
                            when (genre.key) {
                                "action" -> likedGenres[likedGenresIndex++] = 28
                                "adventure" -> likedGenres[likedGenresIndex++] = 12
                                "animation" -> likedGenres[likedGenresIndex++] = 16
                                "comedy" -> likedGenres[likedGenresIndex++] = 35
                                "crime" -> likedGenres[likedGenresIndex++] = 80
                                "documentary" -> likedGenres[likedGenresIndex++] = 99
                                "drama" -> likedGenres[likedGenresIndex++] = 18
                                "family" -> likedGenres[likedGenresIndex++] = 10751
                                "fantasy" -> likedGenres[likedGenresIndex++] = 14
                                "history" -> likedGenres[likedGenresIndex++] = 36
                                "horror" -> likedGenres[likedGenresIndex++] = 27
                                "music" -> likedGenres[likedGenresIndex++] = 10402
                                "mystery" -> likedGenres[likedGenresIndex++] = 9648
                                "romance" -> likedGenres[likedGenresIndex++] = 10749
                                "sci-fi" -> likedGenres[likedGenresIndex++] = 878
                                "tv_movie" -> likedGenres[likedGenresIndex++] = 10770
                                "thriller" -> likedGenres[likedGenresIndex++] = 53
                                "war" -> likedGenres[likedGenresIndex++] = 10752
                                "western" -> likedGenres[likedGenresIndex++] = 37
                            }
                        }
                        if (genre.value.toString().toInt() < 32 && unlikedGenresIndex < 2) {
                            when (genre.key) {
                                "action" -> dislikedGenres[unlikedGenresIndex++] = 28
                                "adventure" -> dislikedGenres[unlikedGenresIndex++] = 12
                                "animation" -> dislikedGenres[unlikedGenresIndex++] = 16
                                "comedy" -> dislikedGenres[unlikedGenresIndex++] = 35
                                "crime" -> dislikedGenres[unlikedGenresIndex++] = 80
                                "documentary" -> dislikedGenres[unlikedGenresIndex++] = 99
                                "drama" -> dislikedGenres[unlikedGenresIndex++] = 18
                                "family" -> dislikedGenres[unlikedGenresIndex++] = 10751
                                "fantasy" -> dislikedGenres[unlikedGenresIndex++] = 14
                                "history" -> dislikedGenres[unlikedGenresIndex++] = 36
                                "horror" -> dislikedGenres[unlikedGenresIndex++] = 27
                                "music" -> dislikedGenres[unlikedGenresIndex++] = 10402
                                "mystery" -> dislikedGenres[unlikedGenresIndex++] = 9648
                                "romance" -> dislikedGenres[unlikedGenresIndex++] = 10749
                                "sci-fi" -> dislikedGenres[unlikedGenresIndex++] = 878
                                "tv_movie" -> dislikedGenres[unlikedGenresIndex++] = 10770
                                "thriller" -> dislikedGenres[unlikedGenresIndex++] = 53
                                "war" -> dislikedGenres[unlikedGenresIndex++] = 10752
                                "western" -> dislikedGenres[unlikedGenresIndex++] = 37
                            }
                        }

                    }
                    Log.i("firebase", "Got value ${it.value}")
                }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }
        }

        runBlocking {
            delay(1500)
            TMDb.init("1373e6da8f4f694cc751405fd528bb62")
            var movieBuilder : Discover.MovieBuilder
            if(likedGenresIndex > 1 && unlikedGenresIndex > 1)
                movieBuilder = Discover.MovieBuilder().sortBy(Discover.SortBy.POPULARITY_DESC).withGenres(likedGenres).withoutGenres(dislikedGenres).primaryReleaseDateLessEqual(
                    LocalDate.now())
            else
            {
                var likedList = List<Int>(1, {i -> maximumGenre})
                var dislikedList = List<Int>(1, {i -> minimumGenre})

                movieBuilder = Discover.MovieBuilder().sortBy(Discover.SortBy.POPULARITY_DESC).
                withGenres(likedList).withoutGenres(dislikedList).primaryReleaseDateLessEqual(
                    LocalDate.now())
            }

            when (val response = TMDb.discoverService.movie(options = movieBuilder)) {
                // Type: NetworkResponse<TmdbPage<TmdbShow.Slim>, TmdbError.DefaultError>
                is NetworkResponse.Success -> {
                    var searchPage: TmdbPage<TmdbMovie.Slim> = response.body
                    var pageNumber : Int = (1..searchPage.totalPages / 2).random()
                    when (val response = TMDb.discoverService.movie(page = pageNumber, options = movieBuilder)) {
                        // Type: NetworkResponse<TmdbPage<TmdbShow.Slim>, TmdbError.DefaultError>
                        is NetworkResponse.Success -> {
                            searchPage = response.body
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