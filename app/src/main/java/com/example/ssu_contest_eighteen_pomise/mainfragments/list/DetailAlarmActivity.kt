package com.example.ssu_contest_eighteen_pomise.mainfragments.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityDetailAlarmBinding

class DetailAlarmActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDetailAlarmBinding
    private val viewModel:DetailAlarmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_detail_alarm)
        binding.lifecycleOwner=this
        binding.viewModel=viewModel

        onViewModelInit()

        viewModel.pillName= intent.getStringExtra(KEY_PILL_NAME).toString()
    }

    fun onViewModelInit() {
        viewModel.finishEvent.observe(this@DetailAlarmActivity, {
            onBackPressed()
        })
    }

    companion object{
        val KEY_PILL_NAME="key_pill_name_for_detail"
    }
}