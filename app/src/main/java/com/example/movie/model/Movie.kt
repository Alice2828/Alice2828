package com.example.movie.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList


@Entity(tableName = "movie_table")
data class Movie(
//    @PrimaryKey
//    @SerializedName("table_id")
//    val tableId:Int,
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("popularity")
    val populatiry: Double,
    @SerializedName("vote_count")
    val vote_count: Int,
    @SerializedName("video")
    val video: Boolean,
    @SerializedName("poster_path")
    val poster_path: String,
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdrop_path: String,
    @SerializedName("original_language")
    val original_language: String,
    @SerializedName("original_title")
    val original_title: String,
//    @SerializedName("genre_ids")
//    val genre_ids: List<Int>,
    @SerializedName("title")
    val title: String,
    @SerializedName("vote_average")
    val vote_average: Double,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("release_date")
    val release_date: String
) : Serializable {

    fun getPosterPath(): String {
        return "https://image.tmdb.org/t/p/w500" + poster_path
    }
}
