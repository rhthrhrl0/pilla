package com.example.ssu_contest_eighteen_pomise.alarm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.alarm.alarm_list_room.AlarmDTO
import com.example.ssu_contest_eighteen_pomise.alarm.alarm_list_room.AlarmDao
import com.example.ssu_contest_eighteen_pomise.alarm.alarm_list_room.AlarmDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    val alarmList = MutableLiveData<List<AlarmDTO>>()
    val finishEvent = MutableLiveData<Boolean>()
    private val db = Room.databaseBuilder(
        application,
        AlarmDatabase::class.java, "database-name-${App.token_prefs.email}"
    ).build()

    init {
        getAlarmList()
    }

    fun getAlarmList() {
        viewModelScope.launch(Dispatchers.IO) {
            alarmList.postValue(db.alarmDao().getAll().sortedWith(compareBy<AlarmDTO> { it.id }).reversed())
        }
    }

    fun updateItem(position:Int){
        viewModelScope.launch(Dispatchers.IO) {
            val alarmDto=AlarmDTO(alarmList.value!![position].title,alarmList.value!![position].body,alarmList.value!![position].receivedTime,true).apply { id=alarmList.value!![position].id }
            db.alarmDao().update(alarmDto)
            getAlarmList()
        }
    }


    fun onClickFinish() {
        finishEvent.value = true
    }
}