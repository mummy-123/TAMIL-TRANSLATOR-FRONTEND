package com.example.tamiltranslatoronboarding.network

data class SignupRequest(
    val full_name: String,   // 🔥 backend expects full_name
    val email: String,
    val password: String
)


data class VerifyRequest(
    val email: String,
    val otp: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class ApiResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null
)

data class SendCodeRequest(
    val email: String
)