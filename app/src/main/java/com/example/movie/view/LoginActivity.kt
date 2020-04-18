package com.example.movie.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.movie.R
import com.example.movie.model.Singleton
import com.example.movie.model.User
import com.example.movie.view_model.LoginViewModel
import com.example.movie.view_model.ViewModelProviderFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import java.lang.reflect.Type

class LoginActivity : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var register: Button
    private lateinit var preferences: SharedPreferences
    private lateinit var json1: String
    private lateinit var emailValue: String
    private lateinit var passwordValue: String
    private lateinit var loginViewModel: LoginViewModel
    private var sessionId: String? = null
    private var accountId: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val viewModelProviderFactory = ViewModelProviderFactory(context = this)
        loginViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(LoginViewModel::class.java)
        bindView()
        stayLogged()

        login.setOnClickListener {
            loginCoroutine()
        }

        register.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
        loginViewModel.liveDataLogin.observe(this, Observer { result ->
            json1 = result[0]
            sessionId = result[1]
            accountId = Integer.parseInt(result[2])

        })
    }

    private fun loginCoroutine() {
        emailValue = email.text.toString()
        passwordValue = password.text.toString()
        if (emailValue != "" && passwordValue != "") {
            loginViewModel.login(emailValue, passwordValue)
            try {
                var MySingleton = sessionId?.let {
                    accountId?.let { it1 ->
                        Singleton.create(
                            emailValue, it,
                            it1
                        )
                    }
                }
                preferences = this@LoginActivity.getSharedPreferences("Username", 0)
                preferences.edit().putString("user", json1).commit()
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {

            }

        } else {
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
            val gsonGen = Gson()
            val json: String? = preferences.getString("user", null)
            val type: Type = object : TypeToken<User>() {}.type
            val user = gsonGen.fromJson<User>(json, type)

            if (user.sessionId != "") {
                var MySingleton =
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
                startActivity(
                    intent
                )
            }
        } catch (e: Exception) {
            Toast.makeText(this@LoginActivity, "You need to log in", Toast.LENGTH_LONG)
                .show()
        }
    }
}

