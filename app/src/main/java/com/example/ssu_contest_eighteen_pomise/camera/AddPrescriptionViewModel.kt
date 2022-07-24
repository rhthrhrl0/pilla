package com.example.ssu_contest_eighteen_pomise.camera

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class AddPrescriptionViewModel(application: Application) : AndroidViewModel(application) {
    val finishEvent=MutableLiveData<Boolean>()
    val ocrEvent=MutableLiveData<Boolean>()
    val selfAddPrescriptionEvent=MutableLiveData<Boolean>()

    fun onClickOCR(){
        ocrEvent.value=true
    }

    fun onClickSelfAddAlarm(){
        selfAddPrescriptionEvent.value=true
    }

    fun onClickFinish() {
        finishEvent.value=true
    }
}