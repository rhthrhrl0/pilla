package com.example.ssu_contest_eighteen_pomise


import android.app.Application
import com.example.ssu_contest_eighteen_pomise.network.LoginService
import com.example.ssu_contest_eighteen_pomise.network.UserService
import com.example.ssu_contest_eighteen_pomise.sharedpreferences.TokenSharedPreferences
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class App : Application() {
    companion object {
        lateinit var token_prefs: TokenSharedPreferences
        lateinit var loginService: LoginService
        lateinit var userService: UserService
    }

    override fun onCreate() {
        token_prefs = TokenSharedPreferences(applicationContext) //초기화.

        val loginRetrofit = Retrofit.Builder()
            .baseUrl(LoginService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(LoginService.getUnsafeOkHttpClient().build())
            .build()
        loginService = loginRetrofit.create(LoginService::class.java)

        val userRetrofit = Retrofit.Builder()
            .baseUrl(UserService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(UserService.getUnsafeOkHttpClient().build())
            .build()
        userService = userRetrofit.create(UserService::class.java)

        super.onCreate()
    }
}