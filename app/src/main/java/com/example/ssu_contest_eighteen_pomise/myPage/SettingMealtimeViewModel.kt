package com.example.ssu_contest_eighteen_pomise.myPage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class SettingMealtimeViewModel(application: Application) : AndroidViewModel(application) {
    var btn_morning = MutableLiveData<Boolean>()
    var btn_lunch = MutableLiveData<Boolean>()
    var btn_dinner = MutableLiveData<Boolean>()
    var btn_finish = MutableLiveData<Boolean>()

    fun btnMorningClick() {
        btn_morning.value = true
    }

    fun btnLunchClick() {
        btn_lunch.value = true
    }

    fun btnDinnerClick() {
        btn_dinner.value = true
    }

    fun onClickFinish() {
        btn_finish.value = true
    }

}