package com.example.movie.model

import com.google.gson.annotations.SerializedName

class Singleton(
    @SerializedName("username")
    var username: String,
    @SerializedName("session_id")
    var sessionId: String,
    @SerializedName("account_id")
    var accountId: Int
) {
    @SerializedName("moviePremier")
    var moviePremier: Movie? = null

    companion object Factory {
        @Volatile
        private var INSTANCE: Singleton? = null

        @Synchronized
        fun create(username: String, session_id: String, account_id: Int):
                Singleton =
            INSTANCE ?: Singleton(username, session_id, account_id).also { INSTANCE = it }

        fun getSession(): String {
            return INSTANCE!!.sessionId
        }

        fun getAccountId(): Int {
            return INSTANCE!!.accountId
        }

        fun getUserName(): String {
            return INSTANCE!!.username
        }
        fun getMovie(): Movie? {
            return INSTANCE!!.moviePremier
        }


        fun setMovie(movie:Movie){
            INSTANCE?.moviePremier=movie
        }

    }
}
