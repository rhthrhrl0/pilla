package com.example.ssu_contest_eighteen_pomise.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.ssu_contest_eighteen_pomise.MainActivity
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityJoinBinding

class JoinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJoinBinding
    private val viewModel: JoinViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        viewModel.backBtn.observe(this@JoinActivity, {
            finish()
        })

        viewModel.joinBtn.observe(this@JoinActivity, {
            finishJoinActivity()
        })

        viewModel.failedJoinToast.observe(this@JoinActivity, {
            Toast.makeText(this, "회원가입에 실패했습니다", Toast.LENGTH_SHORT)
                .show()
        })

    }

    fun finishJoinActivity() {
        Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
        finish()
    }
}