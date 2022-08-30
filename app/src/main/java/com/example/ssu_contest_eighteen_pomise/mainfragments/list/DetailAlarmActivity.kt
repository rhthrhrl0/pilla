package com.example.ssu_contest_eighteen_pomise.mainfragments.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityDetailAlarmBinding
import java.lang.Thread.sleep

class DetailAlarmActivity : AppCompatActivity() {
    //https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15095677

    private lateinit var binding:ActivityDetailAlarmBinding
    private val viewModel:DetailAlarmViewModel by viewModels()
    private var adapter = DetailAlarmAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_detail_alarm)
        binding.lifecycleOwner=this
        binding.viewModel=viewModel
        binding.isLoading = true

        initView()
        onViewModelInit()
        viewModel.pillAlarmDto= intent.getSerializableExtra(KEY_PILL_NAME) as AlarmListDTO
        viewModel.getPillDetailInfo()

    }

    private fun initView() {
        binding.detailRecyclerView.apply {
            this.layoutManager = LinearLayoutManager(this@DetailAlarmActivity)
            this.adapter = this@DetailAlarmActivity.adapter
        }
    }

    fun onViewModelInit() {
        viewModel.pillList.observe(this, {
            if(viewModel.pillList?.value?.isEmpty() == true) {
                binding.isEmpty=true
                binding.isLoading = false
            }
            else {
                binding.isEmpty = false
                adapter.updateItems(it)
                binding.isLoading = false
            }
        })

        viewModel.finishEvent.observe(this@DetailAlarmActivity, {
            onBackPressed()
        })
    }

    companion object{
        val KEY_PILL_NAME="key_pill_name_for_detail"
    }
}