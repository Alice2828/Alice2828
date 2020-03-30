package com.example.movie

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.movie.myFragments.MainFragment

class LoginActivity : AppCompatActivity() {
    lateinit var email:EditText
    lateinit  var password:EditText
    lateinit var login:Button
    lateinit var register:Button
    lateinit var preferences:SharedPreferences

//
//    var fragment: FragmentLike = FragmentLike()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        register = findViewById(R.id.register)
        preferences = getSharedPreferences("Userinfo", 0)


        login.setOnClickListener {

                val emailValue = email.getText().toString()
                val passwordValue = password.getText().toString()
                val registeredEmail = preferences.getString("email", "")
                val registeredPassword = preferences.getString("password", "")
//                if (emailValue == registeredEmail && passwordValue == registeredPassword && emailValue!=""&& passwordValue!="")
//                {
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
//
//            }
            if( emailValue.isEmpty()){
                email.error="This field is required"
                email.requestFocus()}
            else if (!emailValue.contains("[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,18}".toRegex()))
            {
                email.error="Please Check and Enter a valid Email Address"
                email.requestFocus()
            }


            else if(passwordValue.isEmpty()) {
                password.error = "This field is required"
                password.requestFocus()
            }

            else if (!passwordValue.contains("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%!\\-_?&])(?=\\S+\$).{8,}".toRegex()))
            {
                password.error="Enter a Password Correctly"
                password.requestFocus()

            }
            else if (emailValue == registeredEmail && passwordValue == registeredPassword)
            {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }
//            else if(!"\\w".toRegex().matches(emailValue) || !passwordValue.contains("\\w".toRegex()))
//                {
//                    Toast.makeText(this, "Empty login or password", Toast.LENGTH_LONG).show()
//                }

        }
        register.setOnClickListener {

                val intent = Intent(this, RegistrationActivity::class.java)
                startActivity(intent)

        }
    }
}
