package com.example.movie.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movie.model.Cinema

@Dao
interface CinemaDao {
    @Query("SELECT * FROM cinema_table")
    fun getAllCinemas(): List<Cinema>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCinema(cinema: Cinema?)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAllCinema(list: List<Cinema>)
}

