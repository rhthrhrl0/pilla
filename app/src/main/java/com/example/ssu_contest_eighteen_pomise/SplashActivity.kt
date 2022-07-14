package com.example.ssu_contest_eighteen_pomise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.viewModels
import com.example.ssu_contest_eighteen_pomise.auth.LoginActivity
import com.example.ssu_contest_eighteen_pomise.network.LoginService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class SplashActivity : AppCompatActivity() {
    private val viewModel:SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val shPre = App.token_prefs

        viewModel.canLogin.observe(this@SplashActivity,{
            AutoLogin()
        })

        viewModel.canNotLogin.observe(this@SplashActivity,{
            HaveToLogin()
        })

        viewModel.isCanLogin(shPre)
    }

    fun AutoLogin() {
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }, 3000)
    }

    fun HaveToLogin() {
        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }, 3000)
    }
}