package com.example.ssu_contest_eighteen_pomise.mainfragments.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class DetailAlarmViewModel(application: Application) :AndroidViewModel(application) {
    val finishEvent = MutableLiveData<Boolean>()
    var pillName:String=""

    fun onClickFinish() {
        finishEvent.value = true
    }
}