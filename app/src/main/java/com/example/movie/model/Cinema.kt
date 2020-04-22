package com.example.movie.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "cinema_table")
data class Cinema(
    @PrimaryKey
    val id: Int? = null,
    val name: String? = null,
    val address: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)