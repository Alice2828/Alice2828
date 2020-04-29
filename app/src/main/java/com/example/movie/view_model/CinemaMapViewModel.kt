package com.example.movie.view_model
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.database.CinemaDao
import com.example.movie.database.CinemaDatabase
import com.example.movie.database.MovieDatabase
import com.example.movie.model.Cinema
import com.example.movie.model.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class CinemaMapViewModel(context:Context
):ViewModel(){
    private val cinemaDao: CinemaDao = CinemaDatabase.getDatabase(context).cinemaDao()
    fun getCinemaList(): List<Cinema>{
        return cinemaDao.getAllCinemas()
    }
    fun addCinemaListToRoom(list: List<Cinema>){
        cinemaDao.insertAllCinemas(list)
    }

    fun addCinema(cinema: Cinema){
        cinemaDao.insert(cinema)
    }
}


