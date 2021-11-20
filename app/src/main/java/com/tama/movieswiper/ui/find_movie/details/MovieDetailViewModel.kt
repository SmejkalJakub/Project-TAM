package com.tama.movieswiper.ui.find_movie.details

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.haroldadmin.cnradapter.NetworkResponse
import com.tama.movieswiper.R
import de.vkay.api.tmdb.TMDb
import de.vkay.api.tmdb.models.TmdbMovie
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.io.InputStream
import java.net.URL

class MovieDetailViewModel : ViewModel() {

    val movieName = MutableLiveData<String>()
    val movieDescription = MutableLiveData<String>()
    val originalTitle = MutableLiveData<String>()
    val movieRating = MutableLiveData<String>()
    val basicMovieInfo = MutableLiveData<String>()
    val moviePoster = MutableLiveData<Bitmap>()
    val movieRuntime = MutableLiveData<String>()

    val movieGenres = MutableLiveData<String>()

    lateinit var foundedMovie : TmdbMovie

    fun getMovieDetailedInfo(movieId: String)
    {
        var movieIdInt = movieId.toInt()
        runBlocking {
            TMDb.init("1373e6da8f4f694cc751405fd528bb62")

            when (val response = TMDb.movieService.details(movieIdInt!!, "en")) {
                // Type: NetworkResponse<TmdbPage<TmdbShow.Slim>, TmdbError.DefaultError>
                is NetworkResponse.Success -> {
                    foundedMovie = response.body

                    movieName.postValue(foundedMovie.title)
                    originalTitle.postValue(foundedMovie.originalTitle)
                    movieDescription.postValue(foundedMovie.overview)

                    var genres = ""

                    for(genre in foundedMovie.genres)
                    {
                        genres += "${genre.name} / "
                    }
                    genres = genres.removeSuffix(" / ")

                    movieGenres.postValue(genres)

                    var movieInfo = ""

                    for(country in foundedMovie.productionCountries)
                    {
                        movieInfo += "${country.name} / "
                    }

                    movieInfo = movieInfo.removeSuffix(" / ")


                    movieInfo += " ${foundedMovie.releaseDate?.date?.year}, ${foundedMovie.runtime} min"
                    basicMovieInfo.postValue(movieInfo)

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
        var imageUrl = "https://image.tmdb.org/t/p/w342${foundedMovie.poster?.filePath}"

        val w: Int = 150
        val h: Int = 150
        val conf = Bitmap.Config.ARGB_8888 // see other conf types
        var bitmap: Bitmap = Bitmap.createBitmap(w, h, conf)

        var inputStream: InputStream? = null

        try {
            inputStream = URL(imageUrl).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        moviePoster.postValue(bitmap)
    }
}