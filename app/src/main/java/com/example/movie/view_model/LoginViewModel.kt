package com.example.movie.view_model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movie.BuildConfig
import com.example.movie.api.RequestToken
import com.example.movie.api.RetrofitService
import com.example.movie.api.Session
import com.example.movie.database.MovieDao
import com.example.movie.database.MovieDatabase
import com.example.movie.model.MyAccount
import com.example.movie.model.User
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LoginViewModel(private val context: Context) : ViewModel(), CoroutineScope {
    private val job = Job()
    private lateinit var requestToken: String
    private lateinit var newRequestToken: String
    var liveDataLogin = MutableLiveData<List<String>>()
    private var dataLogin = ArrayList<String>()
    private var json1: String = ""
    private var movieDao: MovieDao
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    init {
        movieDao = MovieDatabase.getDatabase(context = context).movieDao()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun login(emailValue: String, passwordValue: String) {
        launch {
            val response = RetrofitService.getPostApi()
                .getRequestTokenCorountine(BuildConfig.THE_MOVIE_DB_API_TOKEN)
            if (response.isSuccessful) {
                requestToken = response.body()?.requestToken!!
                val body = JsonObject().apply {
                    addProperty("username", emailValue)
                    addProperty("password", passwordValue)
                    addProperty("request_token", requestToken)
                }

                val responseLogin = RetrofitService.getPostApi()
                    .loginCoroutune(BuildConfig.THE_MOVIE_DB_API_TOKEN, body)

                if (responseLogin.isSuccessful) {
                    val gson = Gson()
                    val newRequesttoken = gson.fromJson(
                        responseLogin.body(),
                        RequestToken::class.java
                    )
                    newRequestToken = newRequesttoken.requestToken
                    val responseSession = RetrofitService.getPostApi()
                        .getSessionCoroutine(BuildConfig.THE_MOVIE_DB_API_TOKEN, body)

                    if (responseSession.isSuccessful) {
                        val gson = Gson()
                        val newSession =
                            gson.fromJson(
                                responseSession.body(),
                                Session::class.java
                            )
                        val sessionId = newSession.sessionId
                        val response = RetrofitService.getPostApi()
                            .getAccountCoroutine(BuildConfig.THE_MOVIE_DB_API_TOKEN, sessionId)
                        if (response.isSuccessful) {
                            val gson = Gson()
                            val newIdAcc =
                                gson.fromJson(response.body(), MyAccount::class.java)
                            val idAcc = newIdAcc.id
                            val user = User(emailValue, sessionId, idAcc)

                            json1 = gson.toJson(user)
                            dataLogin.add(json1)
                            dataLogin.add(sessionId)
                            dataLogin.add(idAcc.toString())
                        }
                    }
                }
            }
            liveDataLogin.postValue(dataLogin)
        }
    }
}