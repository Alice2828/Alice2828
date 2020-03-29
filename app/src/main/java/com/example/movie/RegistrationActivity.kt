package com.example.movie

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.view.View
import android.content.Intent
import android.widget.Toast;

class RegistrationActivity : AppCompatActivity() {
    lateinit var email: EditText
    lateinit  var password: EditText
    lateinit var name:EditText
    lateinit var login: Button
    lateinit var register: Button
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        register = findViewById(R.id.register)
        name = findViewById(R.id.name)
        preferences = getSharedPreferences("Userinfo", 0)

        register.setOnClickListener {

                val emailValue = email.getText().toString()
                val passwordValue = password.getText().toString()
                val nameValue = name.getText().toString()
                val editor = preferences.edit()
                editor.putString("email", emailValue)
                editor.putString("password", passwordValue)
                editor.putString("name", nameValue)
                editor.apply()
                Toast.makeText(this@RegistrationActivity, "User registered", Toast.LENGTH_SHORT).show()

        }

        login.setOnClickListener{

                val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                startActivity(intent)

        }


    }
}