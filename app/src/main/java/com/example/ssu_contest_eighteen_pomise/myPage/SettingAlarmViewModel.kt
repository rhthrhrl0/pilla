package com.example.ssu_contest_eighteen_pomise.myPage

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.ssu_contest_eighteen_pomise.sharedpreferences.SettingSharedPreferences
import com.yourssu.design.system.atom.ListToggleItem
import com.yourssu.design.system.atom.Toggle

class SettingAlarmViewModel(application: Application) : AndroidViewModel(application) {
    var btn_finish = MutableLiveData<Boolean>()
    var initSoundBtnOn = MutableLiveData<Boolean>()
    var initSoundBtnOff = MutableLiveData<Boolean>()
    var initVibrationBtnOn = MutableLiveData<Boolean>()
    var initVibrationBtnOff = MutableLiveData<Boolean>()
    var toastSoundOn = MutableLiveData<Boolean>()
    var toastSoundOff = MutableLiveData<Boolean>()
    var toastVibrationOn = MutableLiveData<Boolean>()
    var toastVibrationOff = MutableLiveData<Boolean>()
    var isSoundToggleSelected = MutableLiveData<Boolean>()
    var isVibrationToggleSelected = MutableLiveData<Boolean>()
    private lateinit var setting_prefs:SettingSharedPreferences

    fun onClickFinish() {
        btn_finish.value = true
    }

    fun initAlarmBtn() {
        setting_prefs = SettingSharedPreferences
        Log.d("kyb", "현재 소리:"+setting_prefs.sound.toString())
        Log.d("kyb", "현재 진동:"+setting_prefs.vibrate.toString())
        if(setting_prefs.sound.equals("on"))
            initSoundBtnOn.value = true
        else if(setting_prefs.sound.equals("off"))
            initSoundBtnOff.value = true
        if(setting_prefs.vibrate.equals("on"))
            initVibrationBtnOn.value = true
        else if(setting_prefs.vibrate.equals("off"))
            initVibrationBtnOff.value = true
    }

    val soundToggleSelectedListener = object: Toggle.SelectedListener {
        override fun onSelected(boolean: Boolean) {
            isSoundToggleSelected.value = boolean
            setting_prefs = SettingSharedPreferences

            val shPre = setting_prefs
            if(shPre.sound==null) shPre.sound="off"
            if(isSoundToggleSelected.value==true) {
                shPre.sound = "on"
                toastSoundOn.value = true
            }
            else if(isSoundToggleSelected.value==false) {
                shPre.sound = "off"
                toastSoundOff.value = true
            }
            Log.d("kyb", "sound "+shPre.sound.toString())
        }
    }

    val vibrationToggleSelectedListener = object: Toggle.SelectedListener {
        override fun onSelected(boolean: Boolean) {
            isVibrationToggleSelected.value = boolean
            setting_prefs = SettingSharedPreferences

            val shPre = setting_prefs
            if(shPre.vibrate==null) shPre.vibrate="off"
            if(isVibrationToggleSelected.value==true) {
                shPre.vibrate = "on"
                toastVibrationOn.value = true
            }
            else if(isVibrationToggleSelected.value==false) {
                shPre.vibrate = "off"
                toastVibrationOff.value = true
            }
            Log.d("kyb", "vibrate "+shPre.vibrate.toString())
        }
    }

}