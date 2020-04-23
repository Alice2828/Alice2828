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
    fun getAll(): LiveData<List<Cinema>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cinema: Cinema?)
}

