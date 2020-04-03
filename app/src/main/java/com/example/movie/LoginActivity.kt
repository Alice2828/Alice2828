package com.example.movie

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.movie.api.RequestToken
import com.example.movie.api.RetrofitService
import com.example.movie.api.Session
import com.example.movie.myFragments.MainFragment
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var login: Button
    lateinit var register: Button
    lateinit var preferences: SharedPreferences
    lateinit var requestToken: String
    lateinit var newRequestToken: String
    var first = false
    var second = false
//
//    var fragment: FragmentLike = FragmentLike()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        register = findViewById(R.id.register)
        preferences = getSharedPreferences("", 0)

        login.setOnClickListener {

            val emailValue = email.getText().toString()
            val passwordValue = password.getText().toString()
            preferences = getSharedPreferences(emailValue, 0)
            val registeredEmail = preferences.getString("email", "")
            val registeredPassword = preferences.getString("password", "")

//            if (!"\\w".toRegex().matches(emailValue) || !passwordValue.contains("\\w".toRegex())) {
//                Toast.makeText(this, "Empty login or password", Toast.LENGTH_LONG).show()
//            } else
            if (emailValue != "" && passwordValue != "") {
//                    emailValue == registeredEmail && passwordValue == registeredPassword &&

                RetrofitService.getPostApi().getRequestToken(BuildConfig.THE_MOVIE_DB_API_TOKEN)
                    .enqueue(object : Callback<RequestToken> {
                        override fun onFailure(call: Call<RequestToken>, t: Throwable) {

                        }

                        override fun onResponse(
                            call: Call<RequestToken>,
                            response: Response<RequestToken>
                        ) {

                            if (response.isSuccessful) {
                                // first = true
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
                                                var new_RequestToken = gson?.fromJson(
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
                                                    .getSession(BuildConfig.THE_MOVIE_DB_API_TOKEN, body)
                                                    .enqueue(object : Callback<JsonObject> {
                                                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                                                        }

                                                        override fun onResponse(
                                                            call: Call<JsonObject>,
                                                            response: Response<JsonObject>
                                                        ) {

                                                            if (response.isSuccessful) {

                                                                val sessionId =
                                                                    response.body()?.get("session_id").toString()
                                                                Toast.makeText(
                                                                    this@LoginActivity,
                                                                    sessionId,
                                                                    Toast.LENGTH_LONG
                                                                ).show()
                                                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                                                startActivity(intent)


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
