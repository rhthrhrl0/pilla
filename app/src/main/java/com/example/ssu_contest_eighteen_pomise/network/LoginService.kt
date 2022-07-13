package com.example.ssu_contest_eighteen_pomise.network

import com.example.ssu_contest_eighteen_pomise.dto.LoginUserDTO
import com.example.ssu_contest_eighteen_pomise.dto.PostLoginModel
import com.example.ssu_contest_eighteen_pomise.dto.PostSignUpModel
import com.example.ssu_contest_eighteen_pomise.dto.SignUpDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {
    @POST("v1/signIn")
    suspend fun signInRequest(@Body postDto:PostLoginModel):Response<LoginUserDTO>

    @POST("v1/signUp")
    suspend fun signUpRequest(@Body postDto:PostSignUpModel):Response<SignUpDTO>

    companion object {
        const val BASE_URL =
            "http://3.38.152.42:80/"
        const val USER="USER"
        const val ADMIN="ADMIN"
    }
}