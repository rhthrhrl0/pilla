package com.example.ssu_contest_eighteen_pomise

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.ssu_contest_eighteen_pomise.alarm.alarm_list_room.AlarmDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val shPre = App.token_prefs
    val isGuardianLiveData = MutableLiveData<Boolean>()
    var startHomeFragment = MutableLiveData<Boolean>()
    var startSettingFragment = MutableLiveData<Boolean>()
    var startAddPrescription = MutableLiveData<Boolean>()
    var startPillManagement = MutableLiveData<Boolean>()
    var startAlarmList = MutableLiveData<Boolean>()
    val nameToast = MutableLiveData<Boolean>()
    var nameString = ""
    val noReadAlarmItemExist = MutableLiveData<Boolean>()

    private val db = Room.databaseBuilder(
        application,
        AlarmDatabase::class.java, "database-name-${App.token_prefs.email}"
    ).build()

    init {
        getAlarmList()
        if (shPre.isGuardian == true) {
            // 보호자라면
            isGuardianLiveData.value = true
        } else {
            isGuardianLiveData.value = false
        }

        nameString = shPre.name ?: ""
        nameToast.value = true
    }

    fun replaceHome() {
        startHomeFragment.value = true
    }

    fun replaceSetting() {
        startSettingFragment.value = true
    }

    fun addPrescription() {
        startAddPrescription.value = true
    }

    fun pillManage() {
        startPillManagement.value = true
    }

    fun alarmList() {
        startAlarmList.value = true
    }

    fun getAlarmList() {
        viewModelScope.launch(Dispatchers.IO) {
            val alarmList = db.alarmDao().getAll()
            var isNoReadExist = false
            for (a in alarmList) {
                if (!a.isRead) { //읽지 않은게 존재한다면.
                    noReadAlarmItemExist.postValue(true)
                    isNoReadExist = true
                    break
                }
            }
            if (isNoReadExist == false) {
                noReadAlarmItemExist.postValue(false)
            }
        }
    }
}