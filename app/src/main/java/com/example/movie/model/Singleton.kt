package com.example.movie.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Singleton  (
    @SerializedName("username")
    var username: String,
    @SerializedName("session_id")
    var session_id: String,
    @SerializedName("account_id")
    var account_id: Int
) {


    companion object Factory {
        @Volatile
        private var INSTANCE: Singleton? = null

        @Synchronized
        fun create(username: String, session_id: String, account_id: Int):
                Singleton =
            INSTANCE ?: Singleton(username, session_id, account_id).also { INSTANCE = it }

        fun getSession(): String {
            return INSTANCE!!.session_id
        }

        fun getAccountId(): Int {
            return INSTANCE!!.account_id
        }

        fun getUserName(): String {
            return INSTANCE!!.username
        }

    }
}
