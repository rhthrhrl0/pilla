package com.example.ssu_contest_eighteen_pomise.network

import com.example.ssu_contest_eighteen_pomise.dto.*
import com.example.ssu_contest_eighteen_pomise.mainfragments.patient_list.PatientListResponse
import com.example.ssu_contest_eighteen_pomise.room_db_and_dto.RegisteredPill
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.Body
import retrofit2.http.Header

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
    suspend fun logoutRequest(@Header("Authorization") headerToken: String): Response<Void>

    @POST("v1/registerToken")
    suspend fun registerTokenRequest(
        @Header("Authorization") headerToken: String,
        @Body token: String
    ): Response<Token>

    @POST("v1/deleteToken")
    suspend fun deleteTokenRequest(@Header("Authorization") headerToken: String): Response<Token>

    @POST("v1/registerPill")
    suspend fun registerPillRequest(
        @Header("Authorization") headerToken: String,
        @Body pills: List<RegisteredPill>
    ): Response<List<Int>>

    @GET("v1/getProtege")
    suspend fun getPatientListRequest(@Header("Authorization") headerToken: String): Response<PatientListResponse>

    companion object {
        const val BASE_URL =
            "http://43.200.98.211/"
        const val USER = "USER"
        const val ADMIN = "ADMIN"
    }
}