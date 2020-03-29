package com.example.movie

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.content.Intent
import android.view.View

class LoginActivity : AppCompatActivity() {
    lateinit var email:EditText
    lateinit  var password:EditText
    lateinit var login:Button
    lateinit var register:Button
    lateinit var preferences:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        register = findViewById(R.id.register)
        preferences = getSharedPreferences("Userinfo", 0)
        login.setOnClickListener {

                val emailValue = email.getText().toString()
                val passwordValue = password.getText().toString()
                val registeredEmail = preferences.getString("email", "")
                val registeredPassword = preferences.getString("password", "")
                if (emailValue == registeredEmail && passwordValue == registeredPassword)
                {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

            }
        }
        register.setOnClickListener {

                val intent = Intent(this, RegistrationActivity::class.java)
                startActivity(intent)

        }
    }
}
