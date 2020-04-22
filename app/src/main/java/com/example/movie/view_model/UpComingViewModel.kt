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
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class UpComingViewModel(
    private val context: Context
) : ViewModel(), CoroutineScope {
    private val job = Job()
    private var movieDao: MovieDao
    val liveData = MutableLiveData<Movie>()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    init {
        movieDao = MovieDatabase.getDatabase(context = context).movieDao()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun getMovie() {
        launch {
            try {
                val response = RetrofitService.getPostApi()
                    .getUpComing(BuildConfig.THE_MOVIE_DB_API_TOKEN)
                if (response.isSuccessful) {
                    val result = response.body()?.results?.get(0)
                    liveData.value = result
                    movieDao.insert(result)
                } else {
                    liveData.value = movieDao.getAll()[0]
                }
            } catch (e: Exception) {
                liveData.value = movieDao.getAll()[0]
            }
        }
    }
}
