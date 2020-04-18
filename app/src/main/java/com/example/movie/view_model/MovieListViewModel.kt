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
import com.example.movie.model.Movie
import com.example.movie.model.Singleton
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MovieListViewModel(
    private val context: Context
) : ViewModel(), CoroutineScope {
    private val job = Job()
    private var movieDao: MovieDao
    private var sessionId = Singleton.getSession()
    private var accountId = Singleton.getAccountId()

    val liveData = MutableLiveData<List<Movie>>()

    init {
        movieDao = MovieDatabase.getDatabase(context = context).movieDao()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun fetchData() {
        launch {
            val likesOffline = movieDao.getIdOffline(11)
            for (i in likesOffline) {
                val body = JsonObject().apply {
                    addProperty("media_type", "movie")
                    addProperty("media_id", i)
                    addProperty("favorite", true)
                }
                try {
                    RetrofitService.getPostApi()
                        .rateCoroutine(
                            accountId,
                            BuildConfig.THE_MOVIE_DB_API_TOKEN,
                            sessionId,
                            body
                        )
                } catch (e: Exception) {
                }
            }

            val unLikesOffline = movieDao.getIdOffline(10)

            for (i in unLikesOffline) {
                val body = JsonObject().apply {
                    addProperty("media_type", "movie")
                    addProperty("media_id", i)
                    addProperty("favorite", false)
                }
                try {
                    RetrofitService.getPostApi()
                        .rateCoroutine(
                            accountId,
                            BuildConfig.THE_MOVIE_DB_API_TOKEN,
                            sessionId,
                            body
                        )
                } catch (e: Exception) {
                }
            }

            val unLikeMoviesOffline = movieDao.getMovieOffline(10)
            val newArray: ArrayList<Movie>? = null
            for (movie in unLikeMoviesOffline) {
                movie.liked = 0
                newArray?.add(movie)
            }
            newArray?.let { movieDao.insertAll(it) }

            val likeMoviesOffline = movieDao.getMovieOffline(11)
            val newArraylike: ArrayList<Movie>? = null
            for (movie in likeMoviesOffline) {
                movie.liked = 0
                newArraylike?.add(movie)
            }
            newArraylike?.let { movieDao.insertAll(it) }
        }
    }

    fun getMoviesList() {
        fetchData()
        launch {
            val list = withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitService.getPostApi()
                        .getPopularMovieListCoroutine(BuildConfig.THE_MOVIE_DB_API_TOKEN)
                    if (response.isSuccessful) {
                        Log.d("TAG", "")
                        val result = response.body()?.results
                        val result2 = result!!.subList(1, result.lastIndex)
                        if (!result2.isNullOrEmpty()) {
                            movieDao.insertAll(result)
                        }
                        result2
                    } else {
                        Log.d("TAG", "")
                        movieDao.getAll()
                    }
                } catch (e: Exception) {
                    Log.d("TAG", "")
                    movieDao.getAll()
                }
            }
            liveData.postValue(list)
        }
    }
}