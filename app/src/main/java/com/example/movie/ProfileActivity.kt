package com.example.movie

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.movie.model.Singleton

class ProfileActivity : AppCompatActivity() {
    lateinit var nameInfo: TextView
    lateinit var emailInfo:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        nameInfo = findViewById(R.id.name)
        emailInfo = findViewById(R.id.email)
        val authorizedName = Singleton.getUserName()
      //  val authorizedEmail = Singleton.getString("email", "")
        nameInfo.setText(authorizedName)
      //  emailInfo.setText(authorizedEmail)
//        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)



    }
}
