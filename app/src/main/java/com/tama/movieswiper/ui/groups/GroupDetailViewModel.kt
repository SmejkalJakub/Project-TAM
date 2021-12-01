package com.tama.movieswiper.ui.groups

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.haroldadmin.cnradapter.NetworkResponse
import com.tama.movieswiper.R
import com.tama.movieswiper.database.GenreModel
import com.tama.movieswiper.databinding.GroupDetailFragmentBinding
import com.tama.movieswiper.ui.find_movie.FindMovieViewModel
import de.vkay.api.tmdb.Discover
import de.vkay.api.tmdb.TMDb
import de.vkay.api.tmdb.models.TmdbMovie
import de.vkay.api.tmdb.models.TmdbPage
import kotlinx.coroutines.*
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.time.LocalDate
import kotlin.reflect.full.memberProperties

class GroupDetailViewModel : ViewModel() {

    lateinit var nav : NavController
    var currentMovie: Int = 0

    var cumulativeUserPreferences = GenreModel()

    lateinit var binding: GroupDetailFragmentBinding

    val movieName = MutableLiveData<String>()
    val movieDescription = MutableLiveData<String>()
    val movieRating = MutableLiveData<String>()
    val moviePoster = MutableLiveData<Bitmap>()
    val movieRuntime = MutableLiveData<String>()

    fun showRecommendation(groupName: String?) {
        val database =
            Firebase.database("https://tama-project-26b9d-default-rtdb.europe-west1.firebasedatabase.app/").reference

        runBlocking {
            database.child("groups").child(groupName!!).get().addOnSuccessListener {
                for (user in it.child("users").children) {
                    runBlocking {
                        database.child("users").child(user.key.toString()).child("genres").get()
                            .addOnSuccessListener {
                                for (genre in it.children) {
                                    when (genre.key) {
                                        "action" -> cumulativeUserPreferences.action += genre.value.toString()
                                            .toInt()
                                        "adventure" -> cumulativeUserPreferences.adventure += genre.value.toString()
                                            .toInt()
                                        "animation" -> cumulativeUserPreferences.animation += genre.value.toString()
                                            .toInt()
                                        "comedy" -> cumulativeUserPreferences.comedy += genre.value.toString()
                                            .toInt()
                                        "crime" -> cumulativeUserPreferences.crime += genre.value.toString()
                                            .toInt()
                                        "documentary" -> cumulativeUserPreferences.documentary += genre.value.toString()
                                            .toInt()
                                        "drama" -> cumulativeUserPreferences.drama += genre.value.toString()
                                            .toInt()
                                        "family" -> cumulativeUserPreferences.family += genre.value.toString()
                                            .toInt()
                                        "fantasy" -> cumulativeUserPreferences.fantasy += genre.value.toString()
                                            .toInt()
                                        "history" -> cumulativeUserPreferences.history += genre.value.toString()
                                            .toInt()
                                        "horror" -> cumulativeUserPreferences.horror += genre.value.toString()
                                            .toInt()
                                        "music" -> cumulativeUserPreferences.music += genre.value.toString()
                                            .toInt()
                                        "mystery" -> cumulativeUserPreferences.mystery += genre.value.toString()
                                            .toInt()
                                        "romance" -> cumulativeUserPreferences.romance += genre.value.toString()
                                            .toInt()
                                        "sci-fi" -> cumulativeUserPreferences.sci_fi += genre.value.toString()
                                            .toInt()
                                        "tv_movie" -> cumulativeUserPreferences.tv_movie += genre.value.toString()
                                            .toInt()
                                        "thriller" -> cumulativeUserPreferences.thriller += genre.value.toString()
                                            .toInt()
                                        "war" -> cumulativeUserPreferences.war += genre.value.toString()
                                            .toInt()
                                        "western" -> cumulativeUserPreferences.western += genre.value.toString()
                                            .toInt()
                                    }
                                }
                                process_preferences()
                            }
                            .addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                    }
                }
            }
                .addOnFailureListener {
                    Log.e("firebase", "Error getting data", it)
                }
        }
    }

    fun process_preferences()
    {
        var likedGenres: MutableList<Int> = mutableListOf()
        var dislikedGenres : MutableList<Int> = mutableListOf()

        for(i in 1..2)
        {
            var max = -5
            var min = 100000

            var maxGenre : Int = 0
            var minGenre : Int = 0
            for (genre in GenreModel::class.memberProperties) {
                var value = genre.get(cumulativeUserPreferences).toString().toInt()
                when (genre.name) {
                    "action" ->
                    {
                        if(likedGenres.contains(28) || dislikedGenres.contains(28))
                        {
                            continue
                        }
                        if(value > max)
                        {
                            max = value.toString().toInt()
                            maxGenre = 28
                        }
                        if(value < min)
                        {
                            min = value
                            minGenre = 28
                        }
                    }
                    "adventure" ->
                    {
                        if(likedGenres.contains(12) || dislikedGenres.contains(12))
                        {
                            continue
                        }
                        if(value > max)
                        {
                            max = value.toString().toInt()
                            maxGenre = 12
                        }
                        if(value < min)
                        {
                            min = value
                            minGenre = 12
                        }
                    }
                    "animation" ->
                    {
                        if(likedGenres.contains(16) || dislikedGenres.contains(16))
                        {
                            continue
                        }
                        if(value > max)
                        {
                            max = value.toString().toInt()
                            maxGenre = 16
                        }
                        if(value < min)
                        {
                            min = value
                            minGenre = 16
                        }
                    }
                    "comedy" ->
                    {
                        if(likedGenres.contains(35) || dislikedGenres.contains(35))
                        {
                            continue
                        }
                        if(value > max)
                        {
                            max = value.toString().toInt()
                            maxGenre = 35
                        }
                        if(value < min)
                        {
                            min = value
                            minGenre = 35
                        }
                    }
                    "crime" ->
                    {
                        if(likedGenres.contains(80) || dislikedGenres.contains(80))
                        {
                            continue
                        }
                        if(value > max)
                        {
                            max = value.toString().toInt()
                            maxGenre = 80
                        }
                        if(value < min)
                        {
                            min = value
                            minGenre = 80
                        }
                    }
                    "documentary" ->
                    {
                        if(likedGenres.contains(99) || dislikedGenres.contains(99))
                        {
                            continue
                        }
                        if(value > max)
                        {
                            max = value.toString().toInt()
                            maxGenre = 99
                        }
                        if(value < min)
                        {
                            min = value
                            minGenre = 99
                        }
                    }
                    "drama" ->
                    {
                        if(likedGenres.contains(18) || dislikedGenres.contains(18))
                        {
                            continue
                        }
                        if(value > max)
                        {
                            max = value.toString().toInt()
                            maxGenre = 18
                        }
                        if(value < min)
                        {
                            min = value
                            minGenre = 18
                        }
                    }
                    "family" ->
                    {
                        if(likedGenres.contains(10751) || dislikedGenres.contains(10751))
                        {
                            continue
                        }
                        if(value > max)
                        {
                            max = value.toString().toInt()
                            maxGenre = 10751
                        }
                        if(value < min)
                        {
                            min = value
                            minGenre = 10751
                        }
                    }
                    "fantasy" ->
                    {
                        if(likedGenres.contains(14) || dislikedGenres.contains(14))
                        {
                            continue
                        }
                        if(value > max)
                        {
                            max = value.toString().toInt()
                            maxGenre = 14
                        }
                        if(value < min)
                        {
                            min = value
                            minGenre = 14
                        }
                    }
                    "history" ->
                    {
                        if(likedGenres.contains(36) || dislikedGenres.contains(36))
                        {
                            continue
                        }
                        if(value > max)
                        {
                            max = value.toString().toInt()
                            maxGenre = 36
                        }
                        if(value < min)
                        {
                            min = value
                            minGenre = 36
                        }
                    }
                    "horror" ->
                    {
                        if(likedGenres.contains(27) || dislikedGenres.contains(27))
                        {
                            continue
                        }
                        if(value > max)
                        {
                            max = value.toString().toInt()
                            maxGenre = 27
                        }
                        if(value < min)
                        {
                            min = value
                            minGenre = 27
                        }
                    }
                    "music" ->
                    {
                        if(likedGenres.contains(10402) || dislikedGenres.contains(10402))
                        {
                            continue
                        }
                        if(value > max)
                        {
                            max = value.toString().toInt()
                            maxGenre = 10402
                        }
                        if(value < min)
                        {
                            min = value
                            minGenre = 10402
                        }
                    }
                    "mystery" ->
                    {
                        if(likedGenres.contains(9648) || dislikedGenres.contains(9648))
                        {
                            continue
                        }
                        if(value > max)
                        {
                            max = value.toString().toInt()
                            maxGenre = 9648
                        }
                        if(value < min)
                        {
                            min = value
                            minGenre = 9648
                        }
                    }
                    "romance" ->
                    {
                        if(likedGenres.contains(10749) || dislikedGenres.contains(10749))
                        {
                            continue
                        }
                        if(value > max)
                        {
                            max = value.toString().toInt()
                            maxGenre = 10749
                        }
                        if(value < min)
                        {
                            min = value
                            minGenre = 10749
                        }
                    }
                    "sci-fi" ->
                    {
                        if(likedGenres.contains(878) || dislikedGenres.contains(878))
                        {
                            continue
                        }
                        if(value > max)
                        {
                            max = value.toString().toInt()
                            maxGenre = 878
                        }
                        if(value < min)
                        {
                            min = value
                            minGenre = 878
                        }
                    }
                    "tv_movie" ->
                    {
                        if(likedGenres.contains(10770) || dislikedGenres.contains(10770))
                        {
                            continue
                        }
                        if(value > max)
                        {
                            max = value.toString().toInt()
                            maxGenre = 10770
                        }
                        if(value < min)
                        {
                            min = value
                            minGenre = 10770
                        }
                    }
                    "thriller" ->
                    {
                        if(likedGenres.contains(53) || dislikedGenres.contains(53))
                        {
                            continue
                        }
                        if(value > max)
                        {
                            max = value.toString().toInt()
                            maxGenre = 53
                        }
                        if(value < min)
                        {
                            min = value
                            minGenre = 53
                        }
                    }
                    "war" ->
                    {
                        if(likedGenres.contains(10752) || dislikedGenres.contains(10752))
                        {
                            continue
                        }
                        if(value > max)
                        {
                            max = value.toString().toInt()
                            maxGenre = 10752
                        }
                        if(value < min)
                        {
                            min = value
                            minGenre = 10752
                        }
                    }
                    "western" ->
                    {
                        if(likedGenres.contains(37) || dislikedGenres.contains(37))
                        {
                            continue
                        }
                        if(value > max)
                        {
                            max = value.toString().toInt()
                            maxGenre = 37
                        }
                        if(value < min)
                        {
                            min = value
                            minGenre = 37
                        }
                    }
                }
            }
            likedGenres.add(maxGenre)
            dislikedGenres.add(minGenre)
        }
        Log.e("liked", likedGenres.toString())
        Log.e("Disliked", dislikedGenres.toString())

        var movie : TmdbMovie.Slim

        runBlocking {
            TMDb.init("1373e6da8f4f694cc751405fd528bb62")
            var movieBuilder = Discover.MovieBuilder().sortBy(Discover.SortBy.POPULARITY_DESC).withGenres(likedGenres).withoutGenres(dislikedGenres).primaryReleaseDateLessEqual(
                    LocalDate.now())

            when (val response = TMDb.discoverService.movie(options = movieBuilder)) {
                // Type: NetworkResponse<TmdbPage<TmdbShow.Slim>, TmdbError.DefaultError>
                is NetworkResponse.Success -> {
                    var searchPage: TmdbPage<TmdbMovie.Slim> = response.body
                    var pageNumber : Int = (1..(searchPage.totalPages / 2) / 2).random()
                    when (val response = TMDb.discoverService.movie(page = pageNumber, options = movieBuilder)) {
                        // Type: NetworkResponse<TmdbPage<TmdbShow.Slim>, TmdbError.DefaultError>
                        is NetworkResponse.Success -> {
                            searchPage = response.body
                            movie = searchPage.results.random()

                            getMovieDetails(movie.id)
                        }
                        is NetworkResponse.ServerError -> {
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

    fun getMovieDetails(movieId: Int) {

        runBlocking {
            TMDb.init("1373e6da8f4f694cc751405fd528bb62")

            when (val response = TMDb.movieService.details(movieId, "en")) {
                is NetworkResponse.Success -> {
                    val foundedMovie: TmdbMovie = response.body
                    show_recommended_movie(foundedMovie)

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

    fun show_recommended_movie(movie: TmdbMovie)
    {
        currentMovie = movie.id

        binding.movieTableLayout.visibility = View.VISIBLE

        val w: Int = 150
        val h: Int = 150
        val conf = Bitmap.Config.ARGB_8888 // see other conf types
        var bitmap: Bitmap = Bitmap.createBitmap(w, h, conf)

        var inputStream: InputStream? = null

        var imagePath = "https://image.tmdb.org/t/p/w342/${movie.poster!!.filePath}"


        try {
            inputStream = URL(imagePath).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        binding.groupViewLayout.visibility = View.GONE

        movieName.postValue(movie.title)
        movieDescription.postValue(movie.overview)
        movieRating.postValue("Rating: ${movie.voteAverage}/10 ★")
        moviePoster.postValue(bitmap)
        movieRuntime.postValue("Runtime: ${movie.runtime} minutes" )

    }

    fun setNavigationController(navController: NavController, _binding: GroupDetailFragmentBinding)
    {
        binding = _binding
        nav = navController
    }

    fun getMovieDetailedInfo()
    {
        val bundle = bundleOf(Pair("id", currentMovie.toString()), Pair("fromGroup", true))
        nav.navigate(R.id.navigation_movie_detail, bundle)
    }
}