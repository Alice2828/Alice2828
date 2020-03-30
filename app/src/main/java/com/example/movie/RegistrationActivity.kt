package com.example.movie

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.movie.api.RequestToken
import com.example.movie.api.RetrofitService
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class RegistrationActivity : AppCompatActivity() {
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var name: EditText
    lateinit var login: Button
    lateinit var register: Button
    lateinit var preferences: SharedPreferences
    var requestToken: String = ""
    var myResponse: String? = ""

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

            if (emailValue.isEmpty() || passwordValue.isEmpty() || nameValue.isEmpty()) {
                Toast.makeText(this, "Empty values are invalid", Toast.LENGTH_SHORT).show()

//            }
//            else if (!emailValue.contains("[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,18}".toRegex())) {
//                Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
//            } else if (!passwordValue.contains("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%!\\-_?&])(?=\\S+\$).{8,}".toRegex())) {
//                Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()

            } else
            {
               // getSharedPreferences("Userinfo", 0).edit().clear().apply()
                try {

                    RetrofitService.getPostApi().getRequestToken(BuildConfig.THE_MOVIE_DB_API_TOKEN)
                        .enqueue(object : Callback<RequestToken> {
                            override fun onFailure(call: Call<RequestToken>, t: Throwable) {

                            }

                            override fun onResponse(
                                call: Call<RequestToken>,
                                response: Response<RequestToken>
                            ) {

                                if (response.isSuccessful) {

                                    val requestToken = response.body()?.request_token
                                    Toast.makeText(
                                        this@RegistrationActivity,
                                        requestToken,
                                        Toast.LENGTH_LONG
                                    ).show()


                                    val url =
                                        "https://themoviedb.org/3/authenticate/" + requestToken
                                    Toast.makeText(
                                        this@RegistrationActivity,
                                        url,
                                        Toast.LENGTH_LONG
                                    ).show()


                                    this@RegistrationActivity.runOnUiThread(object :
                                        Runnable {
                                        override fun run() {
                                            val browserIntent = Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse(url)
                                            )
                                            startActivity(browserIntent)
                                        }
                                    })
                                }
                                registration(
                                    editor,
                                    emailValue,
                                    passwordValue,
                                    nameValue
                                )
                            }
                        })


    } catch (e: Exception)
    {
        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT)
    }


}

}

login.setOnClickListener {

    onBackPressed()

}


}

fun registration(
    editor: SharedPreferences.Editor,
    emailValue: String,
    passwordValue: String,
    nameValue: String
) {
    editor.putString("email", emailValue)
    editor.putString("password", passwordValue)
    editor.putString("name", nameValue)

    editor.apply()
    Toast.makeText(this@RegistrationActivity, "User registered", Toast.LENGTH_SHORT)
        .show()
}
}