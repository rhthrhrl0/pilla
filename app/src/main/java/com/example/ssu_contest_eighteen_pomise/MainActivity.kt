package com.example.ssu_contest_eighteen_pomise

import android.content.Context
import android.content.Intent
import android.os.*
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.ssu_contest_eighteen_pomise.alarm.AlarmActivity
import com.example.ssu_contest_eighteen_pomise.camera.AddPrescriptionActivity
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityMainBinding
import com.example.ssu_contest_eighteen_pomise.extensionfunction.slideRightEnterAndJustScaleDown
import com.example.ssu_contest_eighteen_pomise.extensionfunction.slideUpperAndNone
import com.example.ssu_contest_eighteen_pomise.mainfragments.pill_manage.PillManageActivity
import com.yourssu.design.system.component.Toast.Companion.shortToast

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val backKeyHandler = BackKeyHandler(this)
    private val vibrationService: Vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibrationServiceManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibrationServiceManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        //requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        onViewModelInit()
    }

    fun onViewModelInit() {
        viewModel.isGuardianLiveData.observe(this@MainActivity, {
            if (it) {
                binding.addPill.visibility = View.GONE //보호자라면 처방기록 추가 기능은 필요없음.
                binding.pillListMenu.visibility = View.GONE //보호자는 등록된 약 수정 할게 없음. 메뉴 필요없다.
            }
        })

        viewModel.startHomeFragment.observe(this@MainActivity, {
            replaceHomeFragment()
        })
        viewModel.startSettingFragment.observe(this@MainActivity, {
            replaceSettingFragment()
        })
        viewModel.startAddPrescription.observe(this@MainActivity, {
            startAddPrescriptionActivity()
        })

        viewModel.startPillManagement.observe(this@MainActivity, {
            startPillManageActivity()
        })
        // 나중에 알람 받는 기능 추가되면, binding.messageAlarm.의 visibility 설정 해줘야 함.
        viewModel.startAlarmList.observe(this@MainActivity, {
            startAlarmListActivity()
        })

        viewModel.nameToast.observe(this@MainActivity, {
            shortToast("${viewModel.nameString}님 환영합니다.")
        })

    }

    fun replaceHomeFragment() {
        if (findNavController(binding.fragmentContainerView.id).currentDestination?.id == R.id.settingFragment) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrationService.vibrate(VibrationEffect.createOneShot(20, 50))
            }
            findNavController(binding.fragmentContainerView.id).navigate(R.id.action_settingFragment_to_homeFragment)
        }
    }

    fun replaceSettingFragment() {
        if (findNavController(binding.fragmentContainerView.id).currentDestination?.id == R.id.homeFragment) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrationService.vibrate(VibrationEffect.createOneShot(20, 50))
            }
            findNavController(binding.fragmentContainerView.id).navigate(R.id.action_homeFragment_to_settingFragment)
        }
    }

    fun startAddPrescriptionActivity() {
        if (findNavController(binding.fragmentContainerView.id).currentDestination?.id == R.id.settingFragment) {
            findNavController(binding.fragmentContainerView.id).navigate(R.id.action_settingFragment_to_homeFragment)
        }
        val intent = Intent(this, AddPrescriptionActivity::class.java)
        startActivity(intent)
        slideUpperAndNone()
    }

    fun startPillManageActivity() {
        val intent = Intent(this, PillManageActivity::class.java)
        startActivity(intent)
        slideRightEnterAndJustScaleDown()
    }

    fun startAlarmListActivity() {
        val intent = Intent(this, AlarmActivity::class.java)
        startActivity(intent)
        slideRightEnterAndJustScaleDown()
    }

    override fun onBackPressed() {
        backKeyHandler.onBackPressed()
    }


}