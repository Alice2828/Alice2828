package com.example.movie


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.movie.api.RequestToken
import com.example.movie.api.RetrofitService
import com.example.movie.api.Session
import com.example.movie.model.MyAccount
import com.example.movie.model.Singleton
import com.example.movie.model.User
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.lang.reflect.Type
import kotlin.coroutines.CoroutineContext

class LoginActivity : AppCompatActivity(), CoroutineScope by MainScope(){
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var login: Button
    lateinit var register: Button
    lateinit var preferences: SharedPreferences
    lateinit var requestToken: String
    lateinit var newRequestToken: String
    lateinit var emailValue: String
    lateinit var passwordValue: String
    private val job= Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        bindView()
        stayLogged()

        login.setOnClickListener {
            loginCoroutine()
        }

        register.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun loginCoroutine(){
        emailValue = email.text.toString()
        passwordValue=password.text.toString()
        if (emailValue != "" && passwordValue != ""){
            launch {
                val response = RetrofitService.getPostApi().getRequestTokenCorountine(BuildConfig.THE_MOVIE_DB_API_TOKEN)
                if(response.isSuccessful){
                    requestToken = response.body()?.requestToken!!
                    val body = JsonObject().apply {
                        addProperty("username",emailValue)
                        addProperty("password",passwordValue)
                        addProperty("request_token",requestToken)
                    }
                    val responseLogin = RetrofitService.getPostApi().loginCoroutune(BuildConfig.THE_MOVIE_DB_API_TOKEN,body)
                    if(responseLogin.isSuccessful)
                    {
                        var gson = Gson()
                        var new_RequestToken = gson.fromJson(
                            responseLogin.body(),
                            RequestToken::class.java
                        )
                        newRequestToken = new_RequestToken.requestToken
                        val responseSession = RetrofitService.getPostApi().getSessionCoroutine(BuildConfig.THE_MOVIE_DB_API_TOKEN,body)

                        if(responseSession.isSuccessful){
                            var gson = Gson()
                            var new_session =
                                gson.fromJson(
                                    responseSession.body(),
                                    Session::class.java
                                )
                            val sessionId = new_session.sessionId
                            val response = RetrofitService.getPostApi().getAccountCoroutine(BuildConfig.THE_MOVIE_DB_API_TOKEN,sessionId)
                            if(response.isSuccessful){
                                var gson = Gson()
                                var new_idAcc = gson.fromJson(response.body(), MyAccount::class.java)
                                var idAcc = new_idAcc.id
                                var user = User(emailValue,sessionId,idAcc)
                                var singleton = Singleton.create(emailValue,sessionId,idAcc)
                                val json1 = gson.toJson(user)
                                preferences = this@LoginActivity.getSharedPreferences("Username",0)
                                preferences.edit().putString("user",json1).commit()
                                val intent = Intent(this@LoginActivity,MainActivity::class.java)
                                startActivity(intent)
                            }
                            else{
                                Toast.makeText(
                                    this@LoginActivity,
                                    "fail",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
        else{
            Toast.makeText(this@LoginActivity, "Empty email or password", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun bindView() {
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        register = findViewById(R.id.register)
    }

    private fun stayLogged() {
        try {
            preferences = this@LoginActivity.getSharedPreferences("Username", 0)
            var gsonGen = Gson()
            var json: String? = preferences.getString("user", null)
            var type: Type = object : TypeToken<User>() {}.type
            var user = gsonGen.fromJson<User>(json, type)
            if (user.sessionId != "") {
                var singleton =
                    Singleton.create(
                        user.username,
                        user.sessionId,
                        user.accountId
                    )
                val intent =
                    Intent(
                        this@LoginActivity,
                        MainActivity::class.java
                    )
                startActivity(intent)
            }
        }
        catch (e: Exception)
        {
        }
    }
}

