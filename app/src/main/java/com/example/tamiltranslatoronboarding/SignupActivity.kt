package com.example.tamiltranslatoronboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tamiltranslatoronboarding.network.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val etName = findViewById<EditText>(R.id.edtName)
        val etEmail = findViewById<EditText>(R.id.edtEmail)
        val etPassword = findViewById<EditText>(R.id.edtPassword)
        val btnSignup = findViewById<Button>(R.id.btnSignup)
        val txtSignIn = findViewById<TextView>(R.id.txtSignIn)

        btnSignup.setOnClickListener {

            val email = etEmail.text.toString()

            val request = SignupRequest(
                full_name = etName.text.toString(),
                email = email,
                password = etPassword.text.toString()
            )

            ApiClient.api.register(request).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(
                    call: Call<ApiResponse>,
                    response: Response<ApiResponse>
                ) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        val intent = Intent(this@SignupActivity, VerifyEmailActivity::class.java)
                        intent.putExtra("email", email)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@SignupActivity, "Signup failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Toast.makeText(this@SignupActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

        // ✅ ONLY NAVIGATION ADDED
        txtSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}