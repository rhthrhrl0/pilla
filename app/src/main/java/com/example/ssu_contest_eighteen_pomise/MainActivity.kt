package com.example.ssu_contest_eighteen_pomise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.ssu_contest_eighteen_pomise.auth.LoginActivity
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel:MainViewModel by viewModels()
    private val backKeyHandler=BackKeyHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.viewModel=viewModel
        binding.lifecycleOwner=this

        viewModel.logoutVar.observe(this@MainActivity,{
            logoutAndStartLoginActivity()
        })

        viewModel.failedLogoutToast.observe(this@MainActivity,{
            Toast.makeText(this,"로그아웃에 실패했습니다",Toast.LENGTH_SHORT).show()
        })
    }

    fun logoutAndStartLoginActivity(){
        val shPre=App.token_prefs
        shPre.refreshToken=""
        shPre.accessToken=""
        shPre.name=""
        shPre.email=""
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onBackPressed() {
        backKeyHandler.onBackPressed()
    }



}