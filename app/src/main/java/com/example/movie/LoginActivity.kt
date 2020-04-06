package com.example.movie

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.movie.api.RequestToken
import com.example.movie.api.RetrofitService
import com.example.movie.api.Session
import com.example.movie.model.MyAccount
import com.example.movie.model.User
import com.example.movie.myFragments.MainFragment
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class LoginActivity : AppCompatActivity() {
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var login: Button
    lateinit var register: Button
    lateinit var preferences: SharedPreferences
    lateinit var requestToken: String
    lateinit var newRequestToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        register = findViewById(R.id.register)
        preferences = this@LoginActivity.getSharedPreferences("Username", 0)

        var gsonGen =
            Gson()
        var json: String? = preferences.getString("user", null)
        var type: Type = object : TypeToken<User>() {}.type
        var userGen = gsonGen.fromJson<User>(json, type)
            //Toast.makeText(this@LoginActivity, userGen.session_id,Toast.LENGTH_LONG).show()
//        if (userGen.session_id!="") {
//            val intent =
//                Intent(
//                    this@LoginActivity,
//                    MainActivity::class.java
//                )
//            startActivity(
//                intent
//            )
//
//        }


        login.setOnClickListener {





            val emailValue = email.getText().toString()
            val passwordValue = password.getText().toString()


            if (emailValue != "" && passwordValue != "") {
                RetrofitService.getPostApi().getRequestToken(BuildConfig.THE_MOVIE_DB_API_TOKEN)
                    .enqueue(object : Callback<RequestToken> {
                        override fun onFailure(call: Call<RequestToken>, t: Throwable) {

                        }

                        override fun onResponse(
                            call: Call<RequestToken>,
                            response: Response<RequestToken>
                        ) {

                            if (response.isSuccessful) {

                                requestToken = response.body()?.request_token!!
                                Toast.makeText(
                                    this@LoginActivity,
                                    requestToken,
                                    Toast.LENGTH_LONG
                                ).show()
                                val body = JsonObject().apply {
                                    addProperty("username", emailValue)
                                    addProperty("password", passwordValue)
                                    addProperty("request_token", requestToken)
                                }
                                RetrofitService.getPostApi()
                                    .login(BuildConfig.THE_MOVIE_DB_API_TOKEN, body)
                                    .enqueue(object : Callback<JsonObject> {
                                        override fun onFailure(
                                            call: Call<JsonObject>,
                                            t: Throwable
                                        ) {

                                        }

                                        override fun onResponse(
                                            call: Call<JsonObject>,
                                            response: Response<JsonObject>
                                        ) {

                                            if (response.isSuccessful) {

                                                var gson = Gson()
                                                var new_RequestToken = gson.fromJson(
                                                    response.body(),
                                                    RequestToken::class.java
                                                )
                                                newRequestToken = new_RequestToken.request_token
                                                Toast.makeText(
                                                    this@LoginActivity,
                                                    newRequestToken,
                                                    Toast.LENGTH_LONG
                                                ).show()

                                                RetrofitService.getPostApi()
                                                    .getSession(
                                                        BuildConfig.THE_MOVIE_DB_API_TOKEN,
                                                        body
                                                    )
                                                    .enqueue(object : Callback<JsonObject> {
                                                        override fun onFailure(
                                                            call: Call<JsonObject>,
                                                            t: Throwable
                                                        ) {

                                                        }

                                                        override fun onResponse(
                                                            call: Call<JsonObject>,
                                                            response: Response<JsonObject>
                                                        ) {

                                                            if (response.isSuccessful) {

                                                                var gson = Gson()
                                                                var new_session =
                                                                    gson.fromJson(
                                                                        response.body(),
                                                                        Session::class.java
                                                                    )

                                                                val sessionId =
                                                                    new_session.session_id
                                                                Toast.makeText(
                                                                    this@LoginActivity,
                                                                    sessionId,
                                                                    Toast.LENGTH_LONG
                                                                ).show()


                                                                RetrofitService.getPostApi()
                                                                    .getAccount(
                                                                        BuildConfig.THE_MOVIE_DB_API_TOKEN,
                                                                        sessionId
                                                                    )
                                                                    .enqueue(object :
                                                                        Callback<JsonObject> {
                                                                        override fun onFailure(
                                                                            call: Call<JsonObject>,
                                                                            t: Throwable
                                                                        ) {
                                                                            Toast.makeText(
                                                                                this@LoginActivity,
                                                                                "fail",
                                                                                Toast.LENGTH_SHORT
                                                                            ).show()
                                                                        }

                                                                        override fun onResponse(
                                                                            call: Call<JsonObject>,
                                                                            response: Response<JsonObject>
                                                                        ) {

                                                                            if (response.isSuccessful) {


                                                                                var gson =
                                                                                    Gson()
                                                                                var new_idAcc =
                                                                                    gson.fromJson(
                                                                                        response.body(),
                                                                                        MyAccount::class.java
                                                                                    )
                                                                                var idAcc =
                                                                                    new_idAcc.id

                                                                                Toast.makeText(
                                                                                    this@LoginActivity,
                                                                                    "id Acc " + idAcc,
                                                                                    Toast.LENGTH_LONG
                                                                                ).show()


                                                                                val user =
                                                                                    User.create(
                                                                                        emailValue,
                                                                                        sessionId,
                                                                                        idAcc
                                                                                    )
                                                                                val json1: String = gson!!.toJson(user)
                                                                                preferences.edit().putString("user", json1)


                                                                                val intent =
                                                                                    Intent(
                                                                                        this@LoginActivity,
                                                                                        MainActivity::class.java
                                                                                    )
                                                                                startActivity(
                                                                                    intent
                                                                                )


                                                                            }

                                                                        }
                                                                    })


                                                            }

                                                        }
                                                    })


                                            }

                                        }
                                    })

                            }

                        }
                    })


            }
        }


        register.setOnClickListener {

            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)

        }
    }
}
