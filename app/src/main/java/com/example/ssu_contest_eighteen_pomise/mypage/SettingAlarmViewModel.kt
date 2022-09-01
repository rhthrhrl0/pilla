package com.example.ssu_contest_eighteen_pomise.mypage

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.ssu_contest_eighteen_pomise.sharedpreferences.SettingSharedPreferences
import com.yourssu.design.system.atom.Toggle

class SettingAlarmViewModel(application: Application) : AndroidViewModel(application) {
    var btn_finish = MutableLiveData<Boolean>()
    var toastSoundOn = MutableLiveData<Boolean>()
    var toastSoundOff = MutableLiveData<Boolean>()
    var toastVibrationOn = MutableLiveData<Boolean>()
    var toastVibrationOff = MutableLiveData<Boolean>()
    private var setting_prefs = SettingSharedPreferences.setInstance(application)

    var soundSelected = MutableLiveData<Boolean>(setting_prefs.sound.equals("on"))

    var vibrateSelected = MutableLiveData<Boolean>(setting_prefs.vibrate.equals("on"))

    fun onClickFinish() {
        btn_finish.value = true
    }

    val soundToggleSelectedListener = object : Toggle.SelectedListener {
        override fun onSelected(boolean: Boolean) {
            soundSelected.value=boolean

            if (boolean) {
                setting_prefs.sound = "on"
                toastSoundOn.value = true
            } else {
                setting_prefs.sound = "off"
                toastSoundOff.value = true
            }
            Log.d("kyb", "sound " + setting_prefs.sound.toString())
        }
    }

    val vibrationToggleSelectedListener = object : Toggle.SelectedListener {
        override fun onSelected(boolean: Boolean) {
            vibrateSelected.value=boolean

            if (boolean) {
                setting_prefs.vibrate = "on"
                toastVibrationOn.value = true
            } else {
                setting_prefs.vibrate = "off"
                toastVibrationOff.value = true
            }
            Log.d("kyb", "vibrate " + setting_prefs.vibrate.toString())
        }
    }

}