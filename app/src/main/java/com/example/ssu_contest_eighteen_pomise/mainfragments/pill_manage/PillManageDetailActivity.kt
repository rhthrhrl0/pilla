package com.example.ssu_contest_eighteen_pomise.mainfragments.pill_manage

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.SpecificTime
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityPillManageDetailBinding
import com.example.ssu_contest_eighteen_pomise.mainfragments.list.AlarmListDTO
import com.example.ssu_contest_eighteen_pomise.mainfragments.list.DetailAlarmActivity
import com.yourssu.design.system.component.Toast.Companion.shortToast
import com.yourssu.design.system.component.Toast.Companion.toast

class PillManageDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPillManageDetailBinding
    private val viewModel: PillManageDetailViewModel by viewModels()
    private val adapter = EatTimeManageAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pill_manage_detail)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        adapter.setMyItemClickListener(object : EatTimeManageAdapter.MyItemClickListener {
            override fun onItemDeleteClick(position: Int) {
                val t = viewModel.registeredTimeList.value?.elementAt(position)?.specificTime
                    ?: SpecificTime(0, 0, 0)
                val builder = AlertDialog.Builder(this@PillManageDetailActivity)
                builder.setTitle("알람 목록에서 삭제")
                builder.setMessage("정말 ${t.hour}시 ${t.minutes} 시간의 알람을 삭제하시겠습니까?")
                builder.setPositiveButton("확인", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        viewModel.deleteTime(
                            position,
                            viewModel.registeredTimeList.value?.elementAt(position)?.id ?: -1
                        )
                        Toast.makeText(applicationContext,"삭제 했습니다.",Toast.LENGTH_SHORT).show()
                    }
                })
                builder.setNegativeButton("취소", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        Toast.makeText(applicationContext,"삭제 취소",Toast.LENGTH_SHORT).show()
                    }
                })
                builder.create().show()
            }
        })

        binding.timeListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (recyclerView.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_SETTLING
                    && !recyclerView.canScrollVertically(1)) {
                    shortToast(resources.getString(R.string.can_not_load_more_registered_time_list_toast_message))
                }
            }
        })

        binding.timeListRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.timeListRv.adapter = adapter

        viewModel.currentPillSetDTO = intent.getSerializableExtra(KEY_PILL_ALARM) as PillSetDTO
        onViewModelInit()
        viewModel.getRegisteredTimeList()
    }

    fun onViewModelInit() {
        viewModel.registeredTimeList.observe(this@PillManageDetailActivity, {
            if (it.isEmpty()) {
                Toast.makeText(this, "더이상 해당 약에 등록된 시간이 없습니다", Toast.LENGTH_SHORT).show()
                onBackPressed()
            } else {
                // 다이얼로그 띄우기.
                adapter.submitList(it)
            }
        })

        viewModel.finishEvent.observe(this@PillManageDetailActivity, {
            onBackPressed()
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0)
    }

    companion object {
        const val KEY_PILL_ALARM = "key_pill_alarm_"
    }
}