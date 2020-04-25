package com.example.movie.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "cinema_table")
data class Cinema(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("address")
    var address: String,
    @SerializedName("latitude")
    var latitude: Double,
    @SerializedName("longtitude")
    var longitude: Double
)