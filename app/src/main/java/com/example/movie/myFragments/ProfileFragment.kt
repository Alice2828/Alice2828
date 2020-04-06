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
import com.example.movie.MainActivity
import com.example.movie.R
import com.example.movie.api.RetrofitService
import com.example.movie.api.Session

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
        return inflater.inflate(R.layout.activity_profile, container, false) as ViewGroup

    }

     public override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         preferences = activity?.getSharedPreferences("Username", 0)!!
         nameInfo = view.findViewById(R.id.name)
         emailInfo = view.findViewById(R.id.email)
         val authorizedName = preferences.getString("user", "Mino")
         val authorizedEmail = preferences.getString("email", "Poiu")
         nameInfo.setText(authorizedName)
         emailInfo.setText(authorizedEmail)
         logout = view.findViewById(R.id.logout)
         logout.setOnClickListener{
//             RetrofitService.getPostApi().deleteSession(BuildConfig.THE_MOVIE_DB_API_TOKEN)
             val editor = preferences.edit()
             editor.clear()

             val intent = Intent(getActivity(), LoginActivity::class.java)
             startActivity(intent)
         }


    }









    }