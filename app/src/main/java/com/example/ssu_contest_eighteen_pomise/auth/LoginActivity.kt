package com.example.ssu_contest_eighteen_pomise.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.BackKeyHandler
import com.example.ssu_contest_eighteen_pomise.MainActivity
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityLoginBinding
import com.example.ssu_contest_eighteen_pomise.sharedpreferences.SettingSharedPreferences
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val model: LoginViewModel by viewModels() // 코틀린 확장함수임.
    private val backKeyHandler = BackKeyHandler(this)
    private val shPre = App.token_prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = model
        binding.lifecycleOwner = this

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("kmj", "Fetching FCM registration token failed", task.exception)
                Toast.makeText(this, "FCM 토큰에 문제가 발생했습니다.\n잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
                finish()
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            shPre.fcmToken = token
            model.getFcmTokenVar.postValue(true)
            Log.d("kmj", token)
        })

        model.getFcmTokenVar.observe(this@LoginActivity, {
            startPostFcmToken()
        })

        model.loginVar.observe(this@LoginActivity, {
            startPostFcmToken()
        })

        model.realLoginVar.observe(this@LoginActivity,{
            startMainActivity()
        })

        model.joinVar.observe(this@LoginActivity, {
            startJoinActivity()
        })

        model.backVar.observe(this@LoginActivity, {
            finish()
        })

        model.failedLoginToast.observe(this@LoginActivity, {
            Toast.makeText(this, "로그인에 실패했습니다", Toast.LENGTH_SHORT).show()
        })
    }

    fun startJoinActivity() {
        val intent = Intent(this, JoinActivity::class.java)
        //intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun startPostFcmToken() { // 로그인과 토큰 얻기 모두 성공해야 로그인 가능함.
        Log.d("kmj","startPostFcmToken()실행...")
        if (model.loginVar.value == true && model.getFcmTokenVar.value == true) {
            Log.d("kmj","startPostFcmToken()의 안 드디어 실행...")
            model.postFcmToken(model.loginUser.refreshToken,shPre.fcmToken!!)
        }
    }

    fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val shPre = App.token_prefs
        shPre.refreshToken = model.loginUser.refreshToken
        shPre.accessToken = model.loginUser.accessToken
        shPre.email = model.loginUser.email
        shPre.name = model.loginUser.username
        shPre.phoneNumber=model.loginUser.phoneNumber
        shPre.isGuardian=model.loginUser.isGuardian
        startActivity(intent)
    }

    override fun onBackPressed() {
        backKeyHandler.onBackPressed()
    }
}