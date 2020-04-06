package com.example.movie.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User (
   @SerializedName("username")
    var username: String,
   @SerializedName("session_id")
    var session_id: String,
   @SerializedName("account_id")
    var account_id: Int
)





