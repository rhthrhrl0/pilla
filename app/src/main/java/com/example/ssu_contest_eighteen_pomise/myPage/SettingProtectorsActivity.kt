package com.example.ssu_contest_eighteen_pomise.myPage

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivitySettingMealtimeBinding
import com.example.ssu_contest_eighteen_pomise.databinding.ActivitySettingProtectorsBinding

class SettingProtectorsActivity:AppCompatActivity() {
    private lateinit var binding: ActivitySettingProtectorsBinding
    private val viewModel: SettingProtectorsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting_protectors)
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