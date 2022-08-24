package com.example.ssu_contest_eighteen_pomise.mainfragments.pill_manage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityPillManageBinding
import com.example.ssu_contest_eighteen_pomise.extensionfunction.justScaleUpAndLeftExit
import com.example.ssu_contest_eighteen_pomise.extensionfunction.showAskDialog
import com.yourssu.design.system.component.Toast.Companion.shortToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PillManageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPillManageBinding
    private val viewModel: PillManageViewModel by viewModels()
    private val adapter = PillManageListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pill_manage)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.pillListRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.pillListRv.adapter = adapter

        adapter.setMyItemClickListener(object : PillManageListAdapter.MyItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(applicationContext, PillManageDetailActivity::class.java)
                intent.putExtra(
                    PillManageDetailActivity.KEY_PILL_ALARM,
                    viewModel.registeredPillList.value?.elementAt(position)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }

            override fun onLongClick(position: Int) {
                showAskDialog(
                    getString(R.string.title_delete_all_pill_time),
                    viewModel.registeredPillList.value?.get(position)?.pillName + getString(R.string.message_delete_pill_all_time),
                    { viewModel.deleteAllTime(position) }
                )
            }
        })

        binding.pillListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (recyclerView.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_SETTLING
                    && !recyclerView.canScrollVertically(1)
                ) {
                    shortToast(resources.getString(R.string.can_not_load_more_registered_pill_list_toast_message))
                }
            }
        })

        onViewModelInit()
    }

    fun onViewModelInit() {
        viewModel.registeredPillList.observe(this@PillManageActivity, {
            adapter.submitList(it)
        })

        viewModel.finishEvent.observe(this@PillManageActivity, {
            onBackPressed()
        })

        repeatOnStart {
            viewModel.eventFlow.collect {
                handleEvent(it)
            }
        }
    }

    private fun handleEvent(it: PillManageViewModel.MyEvent) {
        when (it) {
            is PillManageViewModel.MyEvent.SuccessDeleteAllEvent -> {
                shortToast("삭제 성공")
            }
            is PillManageViewModel.MyEvent.FailedDeleteAllEvent -> {
                shortToast("삭제하는데 문제가 발생했습니다.")
            }
        }

    }

    fun repeatOnStart(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.getRegisteredPillList()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        justScaleUpAndLeftExit()
    }
}