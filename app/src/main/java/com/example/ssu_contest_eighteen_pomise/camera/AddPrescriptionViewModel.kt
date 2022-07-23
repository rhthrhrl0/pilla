package com.example.ssu_contest_eighteen_pomise.camera

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class AddPrescriptionViewModel(application: Application) : AndroidViewModel(application) {
    val finishEvent=MutableLiveData<Boolean>()

    fun onClickFinish() {
        finishEvent.value=true
    }
}