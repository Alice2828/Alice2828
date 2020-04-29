package com.example.movie.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.movie.R
import com.example.movie.model.Cinema
import com.example.movie.model.Singleton
import com.example.movie.model.User
import com.example.movie.view_model.CinemaMapViewModel
import com.example.movie.view_model.LoginViewModel
import com.example.movie.view_model.ViewModelProviderFactory
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.invoke
import java.lang.reflect.Type
import kotlin.Exception

class LoginActivity : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var register: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var preferences: SharedPreferences
    private var data: String? = null

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var cinemaMapViewModel: CinemaMapViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val viewModelProviderFactory = ViewModelProviderFactory(context = this)
        cinemaMapViewModel = ViewModelProvider(this, viewModelProviderFactory).get(CinemaMapViewModel::class.java)
        cinemaMapViewModel.addCinemaListToRoom(generateList())
        loginViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(LoginViewModel::class.java)
        bindView()
        stayLogged()
        loginViewModel.state.observe(this, Observer {
            when (it) {
                is LoginViewModel.State.ShowLoading -> {
                    progressBar.visibility = ProgressBar.VISIBLE
                }
                is LoginViewModel.State.HideLoading -> {
                    progressBar.visibility = ProgressBar.INVISIBLE
                }
                is LoginViewModel.State.BadResult -> {
                    check()
                }
                is LoginViewModel.State.Result -> {
                    loginCoroutine(it.json)
                }
            }
        })
        register.setOnClickListener {

            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
        login.setOnClickListener {
//            var connectivity = application.getSystemService(
//                Context.CONNECTIVITY_SERVICE
//            )as ConnectivityManager
//            if (connectivity) {
            val bundle = Bundle()
            bundle.putString("my_message", stayLogged().toString())
            firebaseAnalytics.logEvent("login_click_event", bundle)
            loginViewModel.makeToken(email.text.toString(), password.text.toString())
           // }
        }

    }

    private fun check() {
        if (email.text.toString() == "" || password.text.toString() == "") {
            Toast.makeText(this@LoginActivity, "Empty email or password", Toast.LENGTH_LONG)
                .show()
        } else {
            Toast.makeText(this@LoginActivity, "Something is wrong", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun bindView() {
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        register = findViewById(R.id.register)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun loginCoroutine(data: String?) {
        try {
            preferences = this@LoginActivity.getSharedPreferences("Username", 0)
            preferences.edit().putString("user", data).commit()
            val gsonGen = Gson()
            val type: Type = object : TypeToken<User>() {}.type
            val user = gsonGen.fromJson<User>(data, type)
            openApp(user)
        } catch (e: Exception) {
        }
    }

    private fun stayLogged() {
        try {
            preferences = this@LoginActivity.getSharedPreferences("Username", 0)
            val gsonGen = Gson()
            val json: String? = preferences.getString("user", null)
            val type: Type = object : TypeToken<User>() {}.type
            val user = gsonGen.fromJson<User>(json, type)
            openApp(user)

        } catch (e: Exception) {
        }
    }

    private fun openApp(user: User) {
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
    }
    fun generateList():List<Cinema>{
        val list: MutableList<Cinema> = ArrayList()
        var cinema = Cinema(
            1,
            "Kinopark 11 IMAX Esentai",
            "проспект Аль-Фараби, 77/8 Esentai Mall, Алматы 050040",
            43.2184733,
            76.92785965209825
        )
        list.add(cinema)
        cinema = Cinema(
            2,
            "Chaplin Mega Alma-Ata",
            "г. Алматы, Розыбакиева, 263, ТРЦ MEGA Alma-Ata, 2 этаж",
            43.264039,
            76.929475
        )
        list.add(cinema)
        cinema = Cinema(
            3,
            "Bekmambetov Cinema",
            "г. Алматы пр. Абая, 109, МФК «Globus»",
            43.240263,
            76.905654
        )
        list.add(cinema)
        cinema = Cinema(
            4,
            "Киноцентр Арман (ТРЦ Asia Park)",
            "г. Алматы, пр. Райымбека 514а, уг. ул. Саина, Торгово-развлекательный центр «Asia Park»",
            43.242221,
            76.957644
        )
        list.add(cinema)
        cinema = Cinema(
            5,
            "LUMIERA Cinema",
            "г. Алматы, пр. Абылай хана, 62, «Арбат»",
            43.262118,
            76.941373
        )
        list.add(cinema)
        cinema = Cinema(
            6,
            "CINEMAX Dostyk Multiplex / Dolby Atmos 3D",
            "г. Алматы, Самал-2, пр. Достык 111, уг. ул. Жолдасбекова, ТРЦ Dostyk Plaza",
            43.233015,
            76.955765
        )
        list.add(cinema)
        cinema = Cinema(
            7,
            "Кинотеатр Kinoplexx Sary Arka 3D (г. Алматы)",
            "г. Алматы, ул. Алтынсарина, 24",
            43.228496,
            76.857868
        )
        list.add(cinema)
        cinema = Cinema(
            8,
            "ТТИ «Алатау» 3D (г. Алматы)",
            "г. Алматы, микрорайон Нұркент, 6.",
            43.260613,
            76.820057
        )
        list.add(cinema)
        cinema = Cinema(
            9,
            "Kinopark 8 Moskva (г. Алматы)",
            "г. Алматы, пр. Абая, уг. Алтынсарина, ТРЦ MOSKVA Metropolitan",
            43.226886,
            76.864135
        )
        list.add(cinema)
        cinema = Cinema(
            10,
            "Кинотеатр «Цезарь 3D»",
            "г. Алматы, ул. Фурманова, 50, уг. ул. Гоголя",
            43.261020,
            76.946446
        )
        list.add(cinema)
        return list
    }
}

