package com.example.ssu_contest_eighteen_pomise.myPage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class SettingProtectorsViewModel(application: Application) : AndroidViewModel(application) {

    var btn_finish = MutableLiveData<Boolean>()

    fun onClickFinish() {
        btn_finish.value = true
    }
}