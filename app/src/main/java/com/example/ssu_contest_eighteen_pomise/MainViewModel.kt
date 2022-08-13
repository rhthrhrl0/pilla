package com.example.ssu_contest_eighteen_pomise

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) :AndroidViewModel(application) {
    private val shPre = App.token_prefs
    val isGuardianLiveData = MutableLiveData<Boolean>()
    var startHomeFragment=MutableLiveData<Boolean>()
    var startSettingFragment=MutableLiveData<Boolean>()
    var startAddPrescription=MutableLiveData<Boolean>()
    var startPillManagement=MutableLiveData<Boolean>()
    var startAlarmList=MutableLiveData<Boolean>()
    val nameToast=MutableLiveData<Boolean>()
    var nameString=""
    init{
        if (shPre.isGuardian == true) {
            // 보호자라면
            isGuardianLiveData.value=true
        } else {
            isGuardianLiveData.value=false
        }

        nameString=shPre.name?:""
        nameToast.value=true
    }

    fun replaceHome(){
        startHomeFragment.value=true
    }
    fun replaceSetting(){
        startSettingFragment.value=true
    }

    fun addPrescription(){
        startAddPrescription.value=true
    }

    fun pillManage(){
        startPillManagement.value=true
    }

    fun alarmList(){
        startAlarmList.value=true
    }

}