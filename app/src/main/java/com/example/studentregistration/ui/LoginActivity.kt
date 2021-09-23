package com.example.studentregistration.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.studentregistration.Constants
import com.example.studentregistration.R
import com.example.studentregistration.api.SessionManager
import com.example.studentregistration.models.LoginRequest
import com.example.studentregistration.databinding.ActivityLoginBinding
import com.example.studentregistration.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    val loginViewModel: LoginViewModel by viewModels()
    lateinit var prefs: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = getSharedPreferences(Constants.SHAREDPREFS, Context.MODE_PRIVATE)
        binding.tvSignUp.setOnClickListener {
            val intent = Intent(baseContext, MainActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        binding.btnLogin.setOnClickListener {
//            sessionManager = SessionManager(this)
            validate()
//            binding.etLoginEmail.visibility = View.GONE
        }


        loginViewModel.loginLiveData.observe(this, { loginResponse ->
            if (!loginResponse.student_id.isNullOrBlank()) {
//                        Toast.makeText(baseContext, loginResponse.message, Toast.LENGTH_LONG).show()
//                        sessionManager.saveAccessToken(loginResponse.access_token)
//                    binding.tvLogin.visibiity = View.GONE

                val editor = prefs.edit()
                val bearerToken = loginResponse.access_token
                editor.putString(Constants.ACCESSTOKEN, bearerToken)
                editor.putString(Constants.STUDENTID, loginResponse.student_id)
                editor.apply()
                val intent = Intent(baseContext, CoursesActivity::class.java)
                intent.putExtra("STUDENT_ID", loginResponse.student_id)
                startActivity(intent)
            }

        })

        loginViewModel.loginFailedLiveData.observe(this, { error ->
//                    binding.tvLogin.visibiity = View.GONE
            Toast.makeText(baseContext, error, Toast.LENGTH_LONG).show()
        })
    }


    fun validate() {
        if (binding.etLoginEmail.text.toString()
                .isEmpty() || binding.etLoginPassword.text.toString().isEmpty()
        ) {
            binding.etLoginEmail.setError("Email Required")
            binding.etLoginPassword.setError("Password Required")
        } else {
            val loginRequest = LoginRequest(
                email = binding.etLoginEmail.text.toString(),
                password = binding.etLoginPassword.text.toString()
            )
            loginViewModel.loginStudent(loginRequest)

        }
    }
}
