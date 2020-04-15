package com.example.movie.view_model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movie.BuildConfig
import com.example.movie.api.RetrofitService
import com.example.movie.database.MovieDao
import com.example.movie.database.MovieDatabase
import com.example.movie.model.Movie
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

    init {
        movieDao = MovieDatabase.getDatabase(context = context).movieDao()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun getMovieBigCard() {
        launch {

            val list = withContext(Dispatchers.IO)
            {
                try {
                    val response = RetrofitService.getPostApi()
                        .getPopularMovieListCoroutine(BuildConfig.THE_MOVIE_DB_API_TOKEN)
                    if (response.isSuccessful) {
                        val result = response.body()?.results

                        if (!result.isNullOrEmpty()) {
                            movieDao?.insertAll(result)
                        }
                        result
                    } else {
                        movieDao?.getAll() ?: emptyList()
                    }

                } catch (e: Exception) {
                    movieDao?.getAll() ?: emptyList()
                }
            }
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
                        movieDao.getAll() ?: emptyList()
                    }
                } catch (e: Exception) {
                    movieDao.getAll() ?: emptyList()
                }
            }

            liveData.postValue(list)
        }
    }
}