package com.example.ssu_contest_eighteen_pomise.network

import com.example.ssu_contest_eighteen_pomise.dto.*
import retrofit2.Response
import retrofit2.http.*

interface LoginService {
    @POST("v1/signIn")
    suspend fun signInRequest(@Body postDto: PostLoginModel): Response<LoginUserDTO>

    @POST("v1/signUp")
    suspend fun signUpRequest(@Body postDto: PostSignUpModel): Response<SignUpDTO>


    @POST("v1/reissue")
    suspend fun refreshTokenRequest(
        @Header("Authorization") headerToken: String,
        @Query("email") email: String,
        @Query("role") role: String,
        @Query("token") token: String
    ): Response<RefreshDTO>

    @POST("v1/logout")
    suspend fun logoutRequest(@Header("Authorization") headerToken: String):Response<Void>


    companion object {
        const val BASE_URL =
            "http://3.38.152.42:80/"
        const val USER = "USER"
        const val ADMIN = "ADMIN"
    }
}