package com.example.ssu_contest_eighteen_pomise.mypage

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivitySettingAlarmBinding
import com.example.ssu_contest_eighteen_pomise.sharedpreferences.SettingSharedPreferences
import com.yourssu.design.system.rule.normal
import java.lang.Exception
import kotlin.system.exitProcess

class SettingAlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingAlarmBinding
    private lateinit var setting_prefs:SettingSharedPreferences
    private val viewModel: SettingAlarmViewModel by viewModels();
    lateinit var notificationManager: NotificationManager
    lateinit var audioManager: AudioManager
//    val MY_PERMISSION_ACCESS_ALL = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting_alarm)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        SettingSharedPreferences.setInstance(applicationContext)
        setting_prefs = SettingSharedPreferences

        getPermission()
        if(notificationManager.isNotificationPolicyAccessGranted)
            setSound()
        else
            backToPrevPage()

        viewModel.btn_finish.observe(this, {
            backToPrevPage()
        })

        viewModel.initSoundBtnOn.observe(this, {
            initSoundBtnOn()
        })

        viewModel.initSoundBtnOff.observe(this, {
            initSoundBtnOff()
        })

        viewModel.initVibrationBtnOn.observe(this, {
            initVibrationBtnOn()
        })

        viewModel.initVibrationBtnOff.observe(this, {
            initVibrationBtnOff()
        })

        viewModel.toastSoundOn.observe(this, {
            toastSoundOn()
            setSound()
        })

        viewModel.toastSoundOff.observe(this, {
            toastSoundOff()
            setSound()
        })

        viewModel.toastVibrationOn.observe(this, {
            toastVibrationOn()
            setSound()
        })

        viewModel.toastVibrationOff.observe(this, {
            toastVibrationOff()
            setSound()
        })

    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if(requestCode==MY_PERMISSION_ACCESS_ALL) {
//            if(grantResults.size>0) {
//                for(grant in grantResults)
//                    if(grant!=PackageManager.PERMISSION_GRANTED)
//                        System.exit(0)
//            }
//        }
//    }

    private fun getPermission() {
        notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        if(!notificationManager.isNotificationPolicyAccessGranted) {
//            var permission = arrayOf(
//                android.Manifest.permission.ACCESS_NOTIFICATION_POLICY
//            )
//            ActivityCompat.requestPermissions(this, permission, MY_PERMISSION_ACCESS_ALL)
            try {
                intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                startActivity(intent)
            } catch(e:Exception) {
                e.printStackTrace()
                exitProcess(0)
            }
        }
    }

    private fun setSound() {
        if(setting_prefs.sound.equals("off") && setting_prefs.vibrate.equals("off"))
            audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT

        if(setting_prefs.vibrate.equals("on"))
            audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE

        if(setting_prefs.sound.equals("on"))
            audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL

    }

    private fun toastSoundOn() {
        Toast.makeText(this, "소리가 켜졌습니다", Toast.LENGTH_SHORT).show()
    }

    private fun toastSoundOff() {
        Toast.makeText(this, "소리가 꺼졌습니다", Toast.LENGTH_SHORT).show()
    }

    private fun toastVibrationOn() {
        Toast.makeText(this, "진동이 켜졌습니다", Toast.LENGTH_SHORT).show()
    }

    private fun toastVibrationOff() {
        Toast.makeText(this, "진동이 꺼졌습니다", Toast.LENGTH_SHORT).show()
    }

    private fun initSoundBtnOn() {
        binding.soundBtn.setSelected(true)
    }

    private fun initSoundBtnOff() {
        binding.soundBtn.setSelected(false)
    }

    private fun initVibrationBtnOn() {
        binding.vibrationBtn.setSelected(true)
    }

    private fun initVibrationBtnOff() {
        binding.vibrationBtn.setSelected(false)
    }

    private fun backToPrevPage() {
        finish()
    }

}