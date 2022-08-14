package com.example.ssu_contest_eighteen_pomise.mainfragments.pill_manage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityPillManageBinding
import com.example.ssu_contest_eighteen_pomise.extensionfunction.justScaleUpAndLeftExit

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