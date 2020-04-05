package com.example.movie

import android.content.Context
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
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class RegistrationActivity : AppCompatActivity() {
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var name: EditText
    lateinit var login: Button
    lateinit var register: Button
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        register = findViewById(R.id.register)
        name = findViewById(R.id.name)

        register.setOnClickListener {

//            val emailValue = email.getText().toString()
//            val passwordValue = password.getText().toString()
//            val nameValue = name.getText().toString()


//            if (emailValue.isEmpty() || passwordValue.isEmpty() || nameValue.isEmpty()) {
//                Toast.makeText(this, "Empty values are invalid", Toast.LENGTH_SHORT).show()

//            }
//            else if (!emailValue.contains("[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,18}".toRegex())) {
//                Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
//            } else if (!passwordValue.contains("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%!\\-_?&])(?=\\S+\$).{8,}".toRegex())) {
//                Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()
//
//            } else
          //  {
                // getSharedPreferences("Userinfo", 0).edit().clear().apply()
//                registration(emailValue, passwordValue, nameValue)
//
//            }

        }

        login.setOnClickListener {

            onBackPressed()

        }


    }

//    fun registration(
//        emailValue: String,
//        passwordValue: String,
//        nameValue: String
//    ) {
//        preferences = getSharedPreferences(emailValue, 0)
//        editor = preferences.edit()
//        editor.putString("email", emailValue)
//        editor.putString("password", passwordValue)
//        editor.putString("name", nameValue)
//
//        editor.apply()
//        Toast.makeText(this@RegistrationActivity, "User registered", Toast.LENGTH_SHORT)
//            .show()
//    }
}