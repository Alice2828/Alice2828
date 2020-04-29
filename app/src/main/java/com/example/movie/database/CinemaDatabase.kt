package com.example.movie.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movie.model.Cinema

@Database(entities = [Cinema::class], version = 1, exportSchema = false)
abstract class CinemaDatabase : RoomDatabase() {
    abstract fun cinemaDao(): CinemaDao

    companion object {
        private var INSTANCE: CinemaDatabase? = null
        fun getDatabase(context: Context): CinemaDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    CinemaDatabase::class.java,
                    "cinema_database.db"
                ).allowMainThreadQueries()
                    .build()
            }
            return INSTANCE!!
        }
    }
}
