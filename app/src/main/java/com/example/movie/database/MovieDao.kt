package com.example.movie.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movie.model.Movie

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Movie>)

    @Query("SELECT*FROM movies_table")
    fun getAll(): List<Movie>

    @Query("SELECT*FROM movies_table  WHERE movies_table.liked = :liked ")
    fun getLiked(liked:Boolean): List<Movie>

//    @Query("UPDATE movies_table SET liked = :liked WHERE id IN (:idList)")
//    fun updateLike(
//        idList: ArrayList<Int?>?,
//        liked: Boolean
//    )


}