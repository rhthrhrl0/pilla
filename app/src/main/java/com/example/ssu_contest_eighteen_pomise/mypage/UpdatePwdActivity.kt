package com.example.ssu_contest_eighteen_pomise.mypage

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityUpdatePasswordBinding

class UpdatePwdActivity:AppCompatActivity() {
    private lateinit var binding: ActivityUpdatePasswordBinding
    private val viewModel: UpdatePwdViewModel by viewModels();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_password)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.btn_finish.observe(this, {
            backToPrevPage()
        })
    }

    private fun backToPrevPage() {
        finish()
    }
}