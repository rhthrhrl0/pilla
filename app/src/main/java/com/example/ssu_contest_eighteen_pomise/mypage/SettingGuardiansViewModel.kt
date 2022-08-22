package com.example.ssu_contest_eighteen_pomise.mypage

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.dto.GuardianInfo
import com.example.ssu_contest_eighteen_pomise.dto.PostLoginModel
import com.example.ssu_contest_eighteen_pomise.network.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class SettingGuardiansViewModel(application: Application) : AndroidViewModel(application) {
    val btn_finish = MutableLiveData<Boolean>()
    var guardianList = MutableLiveData<ArrayList<GuardianInfo>>()

    fun onClickFinish() {
        btn_finish.value = true
    }

    fun retrofit() {
        val shPre = App.token_prefs
        viewModelScope.launch {

            val response = App.userService.getGuardian(shPre.accessToken!!)

            if(response.isSuccessful) {
                val guardianDTO = response.body()!!
                guardianList.value = guardianDTO.guardianInfos as ArrayList<GuardianInfo>
                Log.d("kyb", "guardian 레트로핏 로딩 success : "+response.isSuccessful.toString())
                Log.d("kyb", "레트로핏 전송 내용(getGuardian) : "+response.body().toString())
//                adapter.protectorList = (response.body() as ArrayList<ProtectorDTO>)
//                Log.d("kyb", "size:"+adapter.protectorList.size.toString())
//                Log.d("kyb", "레트로핏 전송 내용2 : "+response.body().toString())
//                protector = (response.body() as ArrayList<ProtectorDTO>?)!!
//                Log.d("kyb", "레트로핏 전송 내용3 : "+response.body() as ArrayList<ProtectorDTO>)
//                adapter.updateItems(response.body()!! as ArrayList<ProtectorDTO>) // 이건 왜 대체 안먹음?
            }
            else {
                Log.d("kyb", "guardian 레트로핏 로딩 error : "+response.errorBody().toString())
            }
        }
    }
}