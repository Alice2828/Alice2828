package com.example.movie.view_model

import android.content.Context
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

class LikeListViewModel(private val context: Context) : ViewModel(), CoroutineScope {
    private val job = Job()
    private var movieDao: MovieDao
    private var sessionId = Singleton.getSession()
    private var accountId = Singleton.getAccountId()

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
}