package com.example.ssu_contest_eighteen_pomise.mypage

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.ssu_contest_eighteen_pomise.sharedpreferences.SettingSharedPreferences
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
    private var setting_prefs = SettingSharedPreferences

    var soundSelected: Boolean = setting_prefs.sound.equals("on")
        set(value){
            field=value
            if (value)
                initSoundBtnOn.value = true
            else
                initSoundBtnOff.value = true
        }

    var vibrateSelected:Boolean = setting_prefs.vibrate.equals("on")
        set(value){
            field=value
            if (value)
                initVibrationBtnOn.value = true
            else
                initVibrationBtnOff.value = true
        }

    fun onClickFinish() {
        btn_finish.value = true
    }

    init {
        soundSelected
    }

//    fun initAlarmBtn() {
//        setting_prefs = SettingSharedPreferences
//        Log.d("kyb", "현재 소리:" + setting_prefs.sound.toString())
//        Log.d("kyb", "현재 진동:" + setting_prefs.vibrate.toString())
//        if (setting_prefs.sound.equals("on"))
//            initSoundBtnOn.value = true
//        else if (setting_prefs.sound.equals("off"))
//            initSoundBtnOff.value = true
//        if (setting_prefs.vibrate.equals("on"))
//            initVibrationBtnOn.value = true
//        else if (setting_prefs.vibrate.equals("off"))
//            initVibrationBtnOff.value = true
//    }

    val soundToggleSelectedListener = object : Toggle.SelectedListener {
        override fun onSelected(boolean: Boolean) {
            isSoundToggleSelected.value = boolean
            setting_prefs = SettingSharedPreferences

            val shPre = setting_prefs
            if (shPre.sound == null) shPre.sound = "off"
            if (isSoundToggleSelected.value == true) {
                shPre.sound = "on"
                toastSoundOn.value = true
            } else if (isSoundToggleSelected.value == false) {
                shPre.sound = "off"
                toastSoundOff.value = true
            }
            Log.d("kyb", "sound " + shPre.sound.toString())
        }
    }

    val vibrationToggleSelectedListener = object : Toggle.SelectedListener {
        override fun onSelected(boolean: Boolean) {
            isVibrationToggleSelected.value = boolean
            setting_prefs = SettingSharedPreferences

            val shPre = setting_prefs
            if (shPre.vibrate == null) shPre.vibrate = "off"
            if (isVibrationToggleSelected.value == true) {
                shPre.vibrate = "on"
                toastVibrationOn.value = true
            } else if (isVibrationToggleSelected.value == false) {
                shPre.vibrate = "off"
                toastVibrationOff.value = true
            }
            Log.d("kyb", "vibrate " + shPre.vibrate.toString())
        }
    }

}