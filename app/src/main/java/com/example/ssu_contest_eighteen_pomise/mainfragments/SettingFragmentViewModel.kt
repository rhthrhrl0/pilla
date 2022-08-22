package com.example.ssu_contest_eighteen_pomise.mainfragments

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.network.LoginService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class SettingFragmentViewModel(application: Application) : AndroidViewModel(application) {
    var logoutVar = MutableLiveData<Boolean>()
    var failedLogoutToast = MutableLiveData<Boolean>()
    var startSettingAlarmActivity=MutableLiveData<Boolean>()
    var startSettingMealtimeActivity=MutableLiveData<Boolean>()
    var startSettingGuardiansActivity=MutableLiveData<Boolean>()
    var startSettingProtegesActivity=MutableLiveData<Boolean>()
    var isGuardian=MutableLiveData<Boolean>()
    lateinit var userEmail:String

    fun settingAlarm(){
        startSettingAlarmActivity.value=true
    }

    fun settingMealtime() {
        startSettingMealtimeActivity.value=true
    }

    fun settingGuardians() {
        startSettingGuardiansActivity.value=true
    }

    fun settingProteges() {
        startSettingProtegesActivity.value=true
    }

    fun getEmail() {
        val shPre = App.token_prefs
        Log.d("kyb", shPre.email.toString())
        userEmail = shPre.email.toString()
    }

    fun isGuardian(){
        val shPre = App.token_prefs
        isGuardian.value = (shPre.isGuardian==true)
    }

    fun logoutBtnClick() {
        val shPre = App.token_prefs
        viewModelScope.launch(Dispatchers.IO) {

            // 아직 리프레쉬 토큰으로 액세스토큰 유효한지 검증 검치지 않음.
            if ((shPre.accessToken != null) and !(shPre.accessToken!!.isEmpty())) {
                App.loginService.deleteTokenRequest(shPre.refreshToken!!)
                val response = App.loginService.logoutRequest(shPre.accessToken!!)
                if (response.isSuccessful) {
                    logoutVar.postValue(true)
                } else {
                    failedLogoutToast.postValue(true)
                }
            } else {
                failedLogoutToast.postValue(true)
            }
        }
    }
}