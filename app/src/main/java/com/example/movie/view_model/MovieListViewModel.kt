package com.example.movie.view_model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movie.BuildConfig
import com.example.movie.api.RetrofitService
import com.example.movie.database.MovieDao
import com.example.movie.database.MovieDatabase
import com.example.movie.model.Movie
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MovieListViewModel(
    private val context: Context
) : ViewModel(), CoroutineScope {
    private val job = Job()
    private var movieDao: MovieDao
    private var accountId: Int? = null
    private var sessionId: String? = ""

    val liveData = MutableLiveData<List<Movie>>()
    val liveDataLike = MutableLiveData<List<Movie>>()

    init {
        movieDao = MovieDatabase.getDatabase(context = context).movieDao()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun getMovieLike() {

        launch {

            val likesOffline = movieDao.getLikedOffline(11)

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


            val unLikesOffline = movieDao.getLikedOffline(10)

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

            val unLikeMoviesOffline = movieDao.getUnLikedOffline()
            val newArray: ArrayList<Movie>? = null
            for (movie in unLikeMoviesOffline) {
                movie.liked = 0
                newArray?.add(movie)
            }
             newArray?.let { movieDao.insertAll(it) }

            val list = withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitService.getPostApi().getFavouriteMoviesCoroutine(
                        accountId,
                        BuildConfig.THE_MOVIE_DB_API_TOKEN,
                        sessionId
                    )
                    if (response.isSuccessful) {
                        val result = response.body()?.results
                        if (result != null) {
                            for (m in result) {
                                m.liked = 1
                            }
                        }
                        if (!result.isNullOrEmpty()) {
                            movieDao.insertAll(result)
                        }
                        result
                    } else {
                        movieDao.getAllLiked()
                    }
                } catch (e: Exception) {
                    movieDao.getAllLiked()
                }
            }
            liveDataLike.postValue(list)
        }
    }

    fun getMoviesList() {
        launch {
            val list = withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitService.getPostApi()
                        .getPopularMovieListCoroutine(BuildConfig.THE_MOVIE_DB_API_TOKEN)
                    if (response.isSuccessful) {
                        val result = response.body()?.results
                        val result2 = result!!.subList(1, result.lastIndex)
                        if (!result2.isNullOrEmpty()) {
                            movieDao.insertAll(result)
                        }
                        val response2 = RetrofitService.getPostApi().getFavouriteMoviesCoroutine(
                            accountId,
                            BuildConfig.THE_MOVIE_DB_API_TOKEN,
                            sessionId
                        )
                        if (response2.isSuccessful) {
                            val result3 = response2.body()?.results
                            for (m in result3!!) {
                                m.liked = 1
                            }
                            if (!result3.isNullOrEmpty()) {
                                movieDao.insertAll(result3)
                            }
                        }
                        result2
                    } else {
                        movieDao.getAll()
                    }
                } catch (e: Exception) {
                    movieDao.getAll()
                }
            }

            liveData.postValue(list)
        }
    }
}