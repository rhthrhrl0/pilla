package com.example.ssu_contest_eighteen_pomise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.ssu_contest_eighteen_pomise.auth.JoinActivity
import com.example.ssu_contest_eighteen_pomise.auth.LoginActivity
import com.example.ssu_contest_eighteen_pomise.camera.AddPrescriptionActivity
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val backKeyHandler = BackKeyHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.startHomeFragment.observe(this@MainActivity, {
            replaceHomeFragment()
        })
        viewModel.startSettingFragment.observe(this@MainActivity, {
            replaceSettingFragment()
        })
        viewModel.startAddPrescription.observe(this@MainActivity, {
            startAddPrescriptionActivity()
        })
        // 나중에 알람 받는 기능 추가되면, binding.messageAlarm.의 visibility 설정 해줘야 함.

    }

    fun replaceHomeFragment() {
        if (findNavController(binding.fragmentContainerView.id).currentDestination?.id == R.id.settingFragment) {
            findNavController(binding.fragmentContainerView.id).navigate(R.id.action_settingFragment_to_homeFragment)
        }
    }

    fun replaceSettingFragment() {
        if (findNavController(binding.fragmentContainerView.id).currentDestination?.id == R.id.homeFragment) {
            findNavController(binding.fragmentContainerView.id).navigate(R.id.action_homeFragment_to_settingFragment)
        }
    }

    fun startAddPrescriptionActivity() {
        if (findNavController(binding.fragmentContainerView.id).currentDestination?.id == R.id.settingFragment) {
            findNavController(binding.fragmentContainerView.id).navigate(R.id.action_settingFragment_to_homeFragment)
        }
        val intent = Intent(this, AddPrescriptionActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        backKeyHandler.onBackPressed()
    }


}