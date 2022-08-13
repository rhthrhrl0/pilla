package com.example.ssu_contest_eighteen_pomise.alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityAlarmBinding
import com.example.ssu_contest_eighteen_pomise.extensionfunction.justScaleUpAndLeftExit

class AlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding
    private val viewModel:AlarmViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_alarm)
        binding.viewModel=viewModel
        binding.lifecycleOwner=this

    }

    override fun onBackPressed() {
        super.onBackPressed()
        justScaleUpAndLeftExit()
    }
}