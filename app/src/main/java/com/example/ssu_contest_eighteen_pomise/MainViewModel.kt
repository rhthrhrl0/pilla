package com.example.ssu_contest_eighteen_pomise

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.ssu_contest_eighteen_pomise.dto.PostLoginModel
import com.example.ssu_contest_eighteen_pomise.network.LoginService
import com.example.ssu_contest_eighteen_pomise.room_db_and_dto.PillDataBase
import com.example.ssu_contest_eighteen_pomise.sharedpreferences.SettingSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainViewModel(application: Application) :AndroidViewModel(application) {
    private val shPre = App.token_prefs
    var startHomeFragment=MutableLiveData<Boolean>()
    var startSettingFragment=MutableLiveData<Boolean>()
    var startAddPrescription=MutableLiveData<Boolean>()
    var startAlarmList=MutableLiveData<Boolean>()
    val nameToast=MutableLiveData<Boolean>()
    var nameString=""
    init{
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

    fun alarmList(){
        startAlarmList.value=true
    }

}