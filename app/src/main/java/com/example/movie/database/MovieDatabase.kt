package com.example.movie.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.movie.model.Movie


@Database(entities = [Movie::class], version = 4,exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {

        var INSTANCE: MovieDatabase? = null


        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE movies_table ADD COLUMN liked BOOLEAN DEFAULT NULL")
            }
        }
        val MIGRATION_1_3: Migration = object : Migration(1, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE movies_table ADD COLUMN liked BOOLEAN DEFAULT 'False'")
            }
        }

        val MIGRATION_1_4: Migration = object : Migration(1, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE movies_table ADD COLUMN liked int DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): MovieDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movies_database.db"
                ).addMigrations(MovieDatabase.MIGRATION_1_4)
                    .build()
            }
            return INSTANCE!!
        }
    }
}