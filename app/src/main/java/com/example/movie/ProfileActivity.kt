package com.example.movie

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity



class ProfileActivity : AppCompatActivity() {
    lateinit var preferences: SharedPreferences
    lateinit var nameInfo: TextView
    lateinit var emailInfo:TextView
    lateinit var logout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
//        preferences = getSharedPreferences("Userinfo", 0)
//        nameInfo = findViewById(R.id.name)
//        emailInfo = findViewById(R.id.email)
//        val authorizedName = preferences.getString("name", "")
//        val authorizedEmail = preferences.getString("email", "")
//        nameInfo.setText(authorizedName)
//        emailInfo.setText(authorizedEmail)

        logout = findViewById(R.id.logout)
//        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
//        logout.setOnClickListener{
//            val editor = preferences.edit()
//            editor.clear()
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//        }









    }
}
