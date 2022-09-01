package com.example.ssu_contest_eighteen_pomise.mypage

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivitySettingAlarmBinding
import com.yourssu.design.system.component.Toast.Companion.shortToast

class SettingAlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingAlarmBinding
    private val viewModel: SettingAlarmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting_alarm)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.btn_finish.observe(this, {
            backToPrevPage()
        })

        viewModel.toastSoundOn.observe(this, {
            toastSoundOn()
        })

        viewModel.toastSoundOff.observe(this, {
            toastSoundOff()
        })

        viewModel.toastVibrationOn.observe(this, {
            toastVibrationOn()
        })

        viewModel.toastVibrationOff.observe(this, {
            toastVibrationOff()
        })

    }

    private fun toastSoundOn() {
        shortToast("소리가 켜졌습니다")
    }

    private fun toastSoundOff() {
        shortToast("소리가 꺼졌습니다")
    }

    private fun toastVibrationOn() {
        shortToast("진동이 켜졌습니다")
    }

    private fun toastVibrationOff() {
        shortToast("진동이 꺼졌습니다")
    }

    private fun backToPrevPage() {
        finish()
    }

}