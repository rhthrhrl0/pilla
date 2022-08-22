package com.example.ssu_contest_eighteen_pomise.network

import com.example.ssu_contest_eighteen_pomise.dto.*
import com.example.ssu_contest_eighteen_pomise.mainfragments.patient_list.PatientListResponse
import com.example.ssu_contest_eighteen_pomise.room_db_and_dto.RegisteredPill
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.Body
import retrofit2.http.Header
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

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
            "https://taewoon.kro.kr/"
        const val USER = "USER"
        const val ADMIN = "ADMIN"
        fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

                }

                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {

                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            })

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            val sslSocketFactory = sslContext.socketFactory

            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { hostname, session -> true }

            return builder
        }
    }
}