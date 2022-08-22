package com.example.ssu_contest_eighteen_pomise.mainfragments.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityDetailAlarmBinding

class DetailAlarmActivity : AppCompatActivity() {
    //https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15095677

    private lateinit var binding:ActivityDetailAlarmBinding
    private val viewModel:DetailAlarmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_detail_alarm)
        binding.lifecycleOwner=this
        binding.viewModel=viewModel

        onViewModelInit()
        Log.d("kmj","여기됨.")
        viewModel.pillAlarmDto= intent.getSerializableExtra(KEY_PILL_NAME) as AlarmListDTO
        viewModel.getPillDetailInfo()
        Log.d("kmj","여기됨.")
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