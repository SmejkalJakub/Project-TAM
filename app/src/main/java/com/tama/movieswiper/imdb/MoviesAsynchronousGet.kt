package com.tama.movieswiper.imdb

import com.tama.movieswiper.ui.find_movie.FindMovieViewModel
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MoviesAsynchronousGet {
    private val client = OkHttpClient()

    var movies = arrayOf<String>("")

    fun getMovieDetails(viewModel: FindMovieViewModel, movieIndex: Int) {

        var movieId = movies[movieIndex]
        val request = Request.Builder()
            .url("https://imdb8.p.rapidapi.com/title/get-overview-details?tconst=$movieId&currentCountry=US")
            .get()
            .addHeader("x-rapidapi-host", "imdb8.p.rapidapi.com")
            .addHeader("x-rapidapi-key", "0a9c46fdddmshff6b1709902e06cp15f7c0jsn97e8ec5449a7")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }

                    var responseString = response.body!!.string()
                    //creating json object
                    val responseJson = JSONObject(responseString)

                    viewModel.changeMovie(movieId, responseJson)
                }
            }
        })
    }

    fun getTopRatedMovies(viewModel: FindMovieViewModel) {

        val request = Request.Builder()
            .url("https://imdb8.p.rapidapi.com/title/get-top-rated-movies")
            .get()
            .addHeader("x-rapidapi-host", "imdb8.p.rapidapi.com")
            .addHeader("x-rapidapi-key", "0a9c46fdddmshff6b1709902e06cp15f7c0jsn97e8ec5449a7")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }

                    var responseString = response.body!!.string()
                    //creating json object
                    val responseJson = JSONArray(responseString)

                    movies = Array<String>(responseJson.length(), { i -> "Number of index: $i"  })

                    for(i in 0 until responseJson.length())
                    {
                        if(i == 0)
                        {

                        }
                        val movieJson = responseJson.getJSONObject(i)

                        var movie = movieJson.getString("id")
                        movie = movie.substring(0, movie.length - 1)
                        var movieId = movie.substringAfterLast('/')
                        movies[i] = movieId
                    }
                    viewModel.loadMovies(movies)
                    getMovieDetails(viewModel, 0)
                }
            }
        })
    }
}