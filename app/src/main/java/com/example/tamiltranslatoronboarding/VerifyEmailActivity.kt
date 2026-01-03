package com.example.tamiltranslatoronboarding

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tamiltranslatoronboarding.network.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifyEmailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_email)

        val email = intent.getStringExtra("email") ?: ""

        val et1 = findViewById<EditText>(R.id.edtOtp1)
        val et2 = findViewById<EditText>(R.id.edtOtp2)
        val et3 = findViewById<EditText>(R.id.edtOtp3)
        val et4 = findViewById<EditText>(R.id.edtOtp4)
        val et5 = findViewById<EditText>(R.id.edtOtp5)
        val et6 = findViewById<EditText>(R.id.edtOtp6)

        val btnVerify = findViewById<Button>(R.id.btnVerify)

        btnVerify.setOnClickListener {

            val otp = et1.text.toString() +
                    et2.text.toString() +
                    et3.text.toString() +
                    et4.text.toString() +
                    et5.text.toString() +
                    et6.text.toString()

            if (otp.length != 6) {
                Toast.makeText(this, "Enter valid OTP", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = VerifyRequest(
                email = email,
                otp = otp
            )

            ApiClient.api.verifyEmail(request).enqueue(object : Callback<ApiResponse> {

                override fun onResponse(
                    call: Call<ApiResponse>,
                    response: Response<ApiResponse>
                ) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        startActivity(Intent(this@VerifyEmailActivity, HomeActivity::class.java))
                        finishAffinity()
                    } else {
                        Toast.makeText(this@VerifyEmailActivity, "Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Toast.makeText(this@VerifyEmailActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}