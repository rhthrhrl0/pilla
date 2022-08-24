package com.example.ssu_contest_eighteen_pomise.alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityAlarmBinding
import com.example.ssu_contest_eighteen_pomise.extensionfunction.justScaleUpAndLeftExit
import com.yourssu.design.system.component.Toast.Companion.shortToast

class AlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding
    private val viewModel: AlarmViewModel by viewModels()
    private val adapter = AlarmManageListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.alarmListRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.alarmListRv.adapter = adapter

        adapter.setMyItemClickListener(object : AlarmManageListAdapter.MyItemClickListener {
            override fun onReadItemClick(position: Int) {

            }

            override fun onNoReadItemClick(position: Int) {
                viewModel.updateItem(position)
            }
        })

        binding.alarmListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (recyclerView.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_SETTLING
                    && !recyclerView.canScrollVertically(1)
                ) {
                    shortToast(resources.getString(R.string.can_not_load_more_receive_alarm_list_toast_message))
                }
            }
        })

        onViewModelInit()
    }

    fun onViewModelInit() {
        viewModel.alarmList.observe(this@AlarmActivity, {
            adapter.submitList(it)
        })

        viewModel.finishEvent.observe(this@AlarmActivity, {
            onBackPressed()
        })

//        repeatOnStart {
//            viewModel.eventFlow.collect {
//                handleEvent(it)
//            }
//        }
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.getAlarmList()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        justScaleUpAndLeftExit()
    }
}