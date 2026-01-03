package com.example.tamiltranslatoronboarding.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("register.php")
    fun register(@Body request: SignupRequest): Call<ApiResponse>

    @POST("login.php")
    fun login(@Body request: LoginRequest): Call<ApiResponse>

    @POST("verify_code.php")
    fun verifyEmail(@Body request: VerifyRequest): Call<ApiResponse>
}