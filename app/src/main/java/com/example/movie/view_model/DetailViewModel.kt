package com.example.movie.view_model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movie.BuildConfig
import com.example.movie.api.RetrofitService
import com.example.movie.database.MovieDao
import com.example.movie.database.MovieDatabase
import com.example.movie.model.FavResponse
import com.example.movie.model.Movie
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class DetailViewModel(private val context: Context) : ViewModel(), CoroutineScope {
    private var movieDao: MovieDao? = null
    val liveData = MutableLiveData<Int>()
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    init {
        movieDao = MovieDatabase.getDatabase(context = context).movieDao()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun haslike(movieId: Int?,sessionId: String?) {
        launch {
            val likeInt = withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitService.getPostApi()
                        .hasLikeCoroutine(
                            movieId,
                            BuildConfig.THE_MOVIE_DB_API_TOKEN, sessionId
                        )
                    Log.d("TAG", response.toString())
                    if (response.isSuccessful) {
                        val gson = Gson()
                        var like = gson.fromJson(
                            response.body(),
                            FavResponse::class.java
                        ).favorite
                        if (like)
                            1
                        else 0
                    } else {
                        movieDao?.getLiked(movieId) ?: 0
                    }
                } catch (e: Exception) {
                    movieDao?.getLiked(movieId) ?: 0
                }
            }
            liveData.postValue(likeInt)
        }
    }

    fun likeMovie(favourite: Boolean, movie: Movie?, movieId: Int?, accountId: Int?, sessionId: String?) {
        launch {
            val body = JsonObject().apply {
                addProperty("media_type", "movie")
                addProperty("media_id", movieId)
                addProperty("favorite", favourite)
            }
            try {
                RetrofitService.getPostApi()
                    .rateCoroutine(
                        accountId,
                        BuildConfig.THE_MOVIE_DB_API_TOKEN, sessionId, body
                    )
            } catch (e: Exception) {
            }
            if (favourite) {
                movie?.liked = 11
                movieDao?.insert(movie)
                Toast.makeText(
                     context,
                    "Movie has been added to favourites",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                movie?.liked = 10
                movieDao?.insert(movie)
                Toast.makeText(
                     context,
                    "Movie has been removed from favourites",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
