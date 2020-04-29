package com.example.movie.myFragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.movie.view.LoginActivity
import com.example.movie.R
import com.example.movie.model.Singleton
import com.example.movie.view_model.ProfileViewModel
import com.example.movie.view_model.ViewModelProviderFactory

class ProfileFragment : Fragment() {
    private lateinit var preferences: SharedPreferences
    private lateinit var nameInfo: TextView
    private lateinit var emailInfo: TextView
    private lateinit var logout: Button
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var progressBar: ProgressBar
    private lateinit var profileListViewModel: ProfileViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val viewModelProviderFactory = ViewModelProviderFactory(context = this.activity as Context)
        profileListViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(ProfileViewModel::class.java)
        val rootView = inflater.inflate(R.layout.activity_profile, container, false) as ViewGroup
        profileListViewModel.liveData.observe(this, Observer {
            when (it) {
                is ProfileViewModel.State.ShowLoading -> {
                    progressBar.visibility = ProgressBar.VISIBLE
                }
                is ProfileViewModel.State.HideLoading -> {
                    progressBar.visibility = ProgressBar.VISIBLE
                }
                is ProfileViewModel.State.Result -> {
                    logout()
                }
                is ProfileViewModel.State.BadResult -> {
                    Toast.makeText(context, "Something is wrong", Toast.LENGTH_LONG).show()
                }
            }
        })
        preferences = context?.getSharedPreferences("Username", 0) as SharedPreferences
        bindView(rootView)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        logout.setOnClickListener {
            profileListViewModel.deleteProfileSession()
        }
    }

    fun logout() {
        editor.clear().commit()
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun bindView(rootView: ViewGroup) {
        nameInfo = rootView.findViewById(R.id.name)
        emailInfo = rootView.findViewById(R.id.email)
        logout = rootView.findViewById(R.id.logout)
        progressBar = rootView.findViewById(R.id.progressBar)
        editor = preferences.edit()
    }

    private fun initViews() {
        val authorizedName = Singleton.getUserName()
        val authorizedEmail = Singleton.getUserName() + "@mail.ru"
        nameInfo.text = authorizedName
        emailInfo.text = authorizedEmail
    }
}