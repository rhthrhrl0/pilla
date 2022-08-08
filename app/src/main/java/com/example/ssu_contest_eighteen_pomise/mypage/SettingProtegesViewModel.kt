package com.example.ssu_contest_eighteen_pomise.mypage

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.dto.PostAddProtegeModel
import com.example.ssu_contest_eighteen_pomise.dto.ProtegeInfo
import com.example.ssu_contest_eighteen_pomise.network.UserService
import com.squareup.moshi.Moshi
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class SettingProtegesViewModel(application: Application) : AndroidViewModel(application) {
    val btn_finish = MutableLiveData<Boolean>()
    var protegeList = MutableLiveData<ArrayList<ProtegeInfo>>()
    var addProtege = MutableLiveData<Boolean>()
    val succeedAddProtege=MutableLiveData<Boolean>()
    val failedAddProtege=MutableLiveData<Boolean>()

    lateinit var protege:PostAddProtegeModel

    fun onClickFinish() {
        btn_finish.value = true
    }

    fun onClickPlus() {
        addProtege.value = true
    }

    fun addProtege(email:String, phoneNum:String) {
        val shPre = App.token_prefs
        Log.d("kyb", "Email:"+email)
        Log.d("kyb", "PhoneNum:"+phoneNum)

        viewModelScope.launch {
            val retrofit = Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            val service = retrofit.create(UserService::class.java)
            val response = service.addProtege(shPre.accessToken!! , PostAddProtegeModel(phoneNum, email))
            if(response.isSuccessful) {
                succeedAddProtege.value = true
                retrofit() //리사이클러뷰 업데이트?
            }
            else {
                Log.d("kyb", "환자 추가 실패 : "+ (response.errorBody()?.string()))
                failedAddProtege.value = true
            }
        }
    }


    fun retrofit() {
        val shPre = App.token_prefs
        viewModelScope.launch {
            val retrofit = Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            val service = retrofit.create(UserService::class.java)
            val response = service.getProtege(shPre.accessToken!!)

            if(response.isSuccessful) {
                val protegeDTO = response.body()!!
                protegeList.value = protegeDTO.protegesInfos as ArrayList<ProtegeInfo>
                Log.d("kyb", "protege 레트로핏 로딩 success : "+response.isSuccessful.toString())
                Log.d("kyb", "레트로핏 전송 내용(getProtege) : "+response.body().toString())
            }
            else {
                Log.d("kyb", "protege 레트로핏 로딩 error : "+ (response.errorBody()?.string()))
            }
        }
    }
}