package com.example.ssu_contest_eighteen_pomise.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.ssu_contest_eighteen_pomise.MainActivity
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val model: LoginViewModel by viewModels() // 코틀린 확장함수임.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = model
        binding.lifecycleOwner = this

        model.joinVar.observe(this@LoginActivity, {
            startJoinActivity()
        })

        model.loginVar.observe(this@LoginActivity, {
            startMainActivity()
        })

        model.backVar.observe(this@LoginActivity, {
            finish()
        })

        model.failedLoginToast.observe(this@LoginActivity, {
            Toast.makeText(this, "회원가입에 실패했습니다", Toast.LENGTH_SHORT).show()
        })
    }

    fun startJoinActivity() {
        val intent = Intent(this, JoinActivity::class.java)
        //intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        // 저장.
        startActivity(intent)
    }

}