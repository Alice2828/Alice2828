package com.example.movie.model

import com.google.gson.annotations.SerializedName

class User private constructor(
    var username: String,
    var session_id: String,
    var account_id: Int
) {


    companion object Factory {
        @Volatile
        private var INSTANCE: User? = null

        @Synchronized
        fun create(username: String, session_id: String, account_id: Int):
                User = INSTANCE?:User(username, session_id, account_id).also{ INSTANCE=it}
        fun getSession():String{
            return INSTANCE!!.session_id
        }
        fun getAccountId():Int{
            return INSTANCE!!.account_id
        }

    }



}
