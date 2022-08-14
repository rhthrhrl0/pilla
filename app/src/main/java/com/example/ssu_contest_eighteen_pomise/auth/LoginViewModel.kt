package com.example.ssu_contest_eighteen_pomise.auth

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.dto.LoginUserDTO
import com.example.ssu_contest_eighteen_pomise.dto.PostLoginModel
import com.example.ssu_contest_eighteen_pomise.dto.Token
import com.example.ssu_contest_eighteen_pomise.network.LoginService
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class LoginViewModel(application: Application) : AndroidViewModel(application) {
    var idString = ""
    var pwdString = ""
    val loginVar=MutableLiveData<Boolean>()
    val getFcmTokenVar=MutableLiveData<Boolean>()
    val joinVar=MutableLiveData<Boolean>()
    val backVar=MutableLiveData<Boolean>()
    val failedLoginToast=MutableLiveData<Boolean>()
    val realLoginVar=MutableLiveData<Boolean>()

    lateinit var loginUser:LoginUserDTO

    fun onPillIdTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        idString = s.toString()
        //isCanPillAdd()
    }
    fun onPillPwdTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        pwdString = s.toString()
        //isCanPillAdd()
    }

    fun loginBtnClick(){
        if (idString.isEmpty()){
            Toast.makeText(getApplication(),"Id를 입력해주세요",Toast.LENGTH_SHORT).show()
        }
        else if(pwdString.isEmpty()){
            Toast.makeText(getApplication(),"비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show()
        }
        else{
            viewModelScope.launch(Dispatchers.IO) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(LoginService.BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()

                //레트로핏 사용할 준비완료.
                val service = retrofit.create(LoginService::class.java)
                val response=service.signInRequest(
                    PostLoginModel(idString.toString(),pwdString.toString())
                )

                Log.d("kmj", "로그인 여부:"+response.isSuccessful.toString())
                if (response.isSuccessful){
                    loginUser= response.body()!!
                    loginVar.postValue(true)
                }
                else{
                    failedLoginToast.postValue(true)
                }

            }
        }
    }

    fun postFcmToken(userToken:String,fcmToken:String){
        viewModelScope.launch(Dispatchers.IO) {
            val retrofit = Retrofit.Builder()
                .baseUrl(LoginService.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            val service = retrofit.create(LoginService::class.java)
            Log.d("kmj","사람의 리프레쉬토큰:"+userToken)
            Log.d("kmj","토큰:"+fcmToken)
            val response=service.registerTokenRequest(userToken,fcmToken)

            Log.d("kmj","토큰 보낸 여부:"+response.isSuccessful.toString())
            if (response.isSuccessful){
                realLoginVar.postValue(true)
            }
            else{
                failedLoginToast.postValue(true)
                Log.d("kmj","토큰 보내기 실패...:"+response.errorBody().toString())
            }
        }
    }

    fun joinBtnClick(){
        joinVar.value=true
    }

    fun backBtn(){
        backVar.value=true
    }
}