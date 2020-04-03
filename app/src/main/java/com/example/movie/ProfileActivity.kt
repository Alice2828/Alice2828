package com.example.movie

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ProfileActivity : AppCompatActivity() {
    lateinit var preferences: SharedPreferences
    lateinit var nameInfo: TextView
    lateinit var emailInfo:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        preferences = getSharedPreferences("Userinfo", 0)
        nameInfo = findViewById(R.id.name)
        emailInfo = findViewById(R.id.email)
        val authorizedName = preferences.getString("name", "")
        val authorizedEmail = preferences.getString("email", "")
        nameInfo.setText(authorizedName)
        emailInfo.setText(authorizedEmail)
//        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)



    }
}
