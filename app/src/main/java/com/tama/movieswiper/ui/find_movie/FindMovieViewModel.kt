package com.tama.movieswiper.ui.find_movie

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.tama.movieswiper.R
import com.tama.movieswiper.database.BasicMovie
import com.tama.movieswiper.database.MovieDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.URL

class FindMovieViewModel : ViewModel() {

    var movies : Array<String> = arrayOf<String>("")

    val movieName = MutableLiveData<String>()
    val movieDescription = MutableLiveData<String>()
    val movieRating = MutableLiveData<String>()
    val moviePoster = MutableLiveData<Bitmap>()

    fun loadMovies(_movies: Array<String>)
    {
        var i: Int = 0
        this.movies = Array<String>(_movies.size, { i -> "Number of index: $i"  })

        for(movie in _movies)
        {
            this.movies?.set(i, movie)
            i++
        }
    }

    fun changeMovie(movieId: String, movieData: JSONObject)
    {
        /*val db = Room.databaseBuilder(
            applicationContext,
            MovieDatabase::class.java, "movie_database"
        ).build()*/

        //val movieDao = db.basicMovieDao

        val w: Int = 150
        val h: Int = 150
        val conf = Bitmap.Config.ARGB_8888 // see other conf types
        var bitmap: Bitmap = Bitmap.createBitmap(w, h, conf)

        val titleJson = JSONObject(movieData.getString("title"))

        val categoriesJson = movieData.getJSONArray("genres")

        val imageJson = JSONObject(titleJson.getString("image"));
        val imageURL = imageJson.getString("url")

        val descriptionJson = JSONObject(movieData.getString("plotOutline"));
        val descriptionText = descriptionJson.getString("text")

        val ratingJson = JSONObject(movieData.getString("ratings"))
        val ratingScore = ratingJson.getString("rating")

        var inputStream: InputStream? = null

        try {
            inputStream = URL(imageURL).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var movie = BasicMovie()

        movie.movieId = movieId
        movie.movieTitle = titleJson.getString("title")
        movie.rating = ratingScore.toDouble()
        movie.imageURL = imageURL

        movieName.postValue(movie.movieTitle)
        movieDescription.postValue(descriptionText)
        movieRating.postValue("Rating $ratingScore")
        moviePoster.postValue(bitmap)


        /*GlobalScope.launch {
            movieDao.insert(movie)
        }

        val db_movies = movieDao.getAllMovies()

        this@_MainActivity.runOnUiThread(java.lang.Runnable {
            var movieTitleText: TextView = findViewById<View>(R.id.movieTitle) as TextView
            var imageView: ImageView = findViewById<View>(R.id.moviePicture) as ImageView
            var movieText: TextView = findViewById<View>(R.id.movieDescription) as TextView
            var movieRatingText: TextView = findViewById<View>(R.id.movieRating) as TextView

            movieTitleText.text = titleJson.getString("title")
            imageView.setImageBitmap(bitmap)
            movieText.text = descriptionText
            movieRatingText.text = "Rating: " + ratingScore
        })*/

    }
}