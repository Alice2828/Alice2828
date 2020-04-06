package com.example.movie.myFragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.movie.BuildConfig
import com.example.movie.LoginActivity
import com.example.movie.R
import com.example.movie.api.RetrofitService
import com.example.movie.model.Singleton
import com.google.gson.JsonObject

class ProfileFragment:Fragment() {
    lateinit var preferences: SharedPreferences
    lateinit var nameInfo: TextView
    lateinit var emailInfo: TextView
    lateinit var logout: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView=inflater.inflate(R.layout.activity_profile, container, false) as ViewGroup

        preferences = activity?.getSharedPreferences("Userinfo", 0)!!
        nameInfo = rootView.findViewById(R.id.name)
        emailInfo = rootView.findViewById(R.id.email)
        val authorizedName = Singleton.getUserName()
       val authorizedEmail = Singleton.getUserName()+"@mail.ru"
        nameInfo.setText(authorizedName)
        emailInfo.setText(authorizedEmail)
        logout = rootView.findViewById(R.id.logout)
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logout.setOnClickListener{
            val body:JsonObject = JsonObject().apply {
                addProperty("session_id", Singleton.getSession())
            }

            RetrofitService.getPostApi().deleteSession(BuildConfig.THE_MOVIE_DB_API_TOKEN, body)
            val editor = preferences.edit()
            editor.clear().commit()

            val intent = Intent(getActivity(), LoginActivity::class.java)
            startActivity(intent)
        }
    }

    }