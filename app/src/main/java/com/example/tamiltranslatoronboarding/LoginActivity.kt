package com.example.tamiltranslatoronboarding

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tamiltranslatoronboarding.network.ApiClient
import com.example.tamiltranslatoronboarding.network.ApiResponse
import com.example.tamiltranslatoronboarding.network.LoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.tamiltranslatoronboarding.session.SessionManager

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.edtLoginEmail)
        val etPassword = findViewById<EditText>(R.id.edtLoginPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val txtSignup = findViewById<TextView>(R.id.txtSignup)

        btnLogin.setOnClickListener {

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = LoginRequest(
                email = email,
                password = password
            )

            ApiClient.api.login(request).enqueue(object : Callback<ApiResponse> {

                override fun onResponse(
                    call: Call<ApiResponse>,
                    response: Response<ApiResponse>
                ) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        SessionManager(this@LoginActivity).setLoggedIn(true)

                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finishAffinity()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            response.body()?.message ?: "Login failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

        // navigation
        txtSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}