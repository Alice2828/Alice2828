package com.example.movie.database

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movie.model.Cinema

interface CinemaDao {
    @Query("SELECT * FROM cinema_table")
    fun getCinemas(): LiveData<List<Cinema>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cinema: Cinema)
}