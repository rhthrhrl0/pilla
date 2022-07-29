package com.example.ssu_contest_eighteen_pomise.myPage

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivitySettingAlarmBinding
import com.example.ssu_contest_eighteen_pomise.sharedpreferences.SettingSharedPreferences
import com.yourssu.design.system.atom.Toggle

class SettingAlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingAlarmBinding
    private lateinit var setting_prefs:SettingSharedPreferences
    private val viewModel: SettingAlarmViewModel by viewModels();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting_alarm)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        SettingSharedPreferences.setInstance(applicationContext)
        setting_prefs = SettingSharedPreferences

        viewModel.initAlarmBtn()

        viewModel.btn_finish.observe(this, {
            backToPrevPage()
        })

        viewModel.initSoundBtnOn.observe(this, {
            initSoundBtnOn()
        })

        viewModel.initSoundBtnOff.observe(this, {
            initSoundBtnOff()
        })

        viewModel.initVibrationBtnOn.observe(this, {
            initVibrationBtnOn()
        })

        viewModel.initVibrationBtnOff.observe(this, {
            initVibrationBtnOff()
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
        Toast.makeText(this, "소리가 켜졌습니다", Toast.LENGTH_SHORT).show()
    }

    private fun toastSoundOff() {
        Toast.makeText(this, "소리가 꺼졌습니다", Toast.LENGTH_SHORT).show()
    }

    private fun toastVibrationOn() {
        Toast.makeText(this, "진동이 켜졌습니다", Toast.LENGTH_SHORT).show()
    }

    private fun toastVibrationOff() {
        Toast.makeText(this, "진동이 꺼졌습니다", Toast.LENGTH_SHORT).show()
    }

    private fun initSoundBtnOn() {
        binding.soundBtn.setSelected(true)
    }

    private fun initSoundBtnOff() {
        binding.soundBtn.setSelected(false)
    }

    private fun initVibrationBtnOn() {
        binding.vibrationBtn.setSelected(true)
    }

    private fun initVibrationBtnOff() {
        binding.vibrationBtn.setSelected(false)
    }

    private fun backToPrevPage() {
        finish()
    }

}