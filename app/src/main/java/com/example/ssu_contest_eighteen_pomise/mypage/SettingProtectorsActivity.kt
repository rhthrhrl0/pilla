package com.example.ssu_contest_eighteen_pomise.mypage

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivitySettingProtectorsBinding
import com.example.ssu_contest_eighteen_pomise.dto.ProtectorDTO

class SettingProtectorsActivity:AppCompatActivity() {
    private lateinit var binding: ActivitySettingProtectorsBinding
    private val viewModel: SettingProtectorsViewModel by viewModels()

    val settingProtectorsAdapter = SettingProtectorsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting_protectors)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        onViewModelInit()
        initView()
    }

    private fun onViewModelInit() {
        viewModel.btn_finish.observe(this, {
            backToPrevPage()
        })

        viewModel.protectorsList.observe(this, {
            settingProtectorsAdapter.updateItems(it)
        })
    }

    private fun initView() {
        settingProtectorsAdapter.updateItems(viewModel.protectorsList.value!!)
    }

    private fun backToPrevPage() {
        finish()
    }
}