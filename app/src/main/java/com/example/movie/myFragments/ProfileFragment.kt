package com.example.movie.myFragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.movie.R

class ProfileFragment:Fragment() {
    lateinit var preferences: SharedPreferences
    lateinit var nameInfo: TextView
    lateinit var emailInfo: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView=inflater.inflate(R.layout.activity_profile, container, false) as ViewGroup

        preferences = activity?.getSharedPreferences("Userinfo", 0)!!
        nameInfo = rootView.findViewById(R.id.name)
        emailInfo = rootView.findViewById(R.id.email)
        val authorizedName = preferences.getString("name", "")
        val authorizedEmail = preferences.getString("email", "")
        nameInfo.setText(authorizedName)
        emailInfo.setText(authorizedEmail)
        return rootView
    }

    }