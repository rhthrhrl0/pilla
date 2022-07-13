package com.example.ssu_contest_eighteen_pomise.auth

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ssu_contest_eighteen_pomise.dto.LoginUserDTO
import com.example.ssu_contest_eighteen_pomise.dto.PostLoginModel
import com.example.ssu_contest_eighteen_pomise.network.LoginService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class LoginViewModel(application: Application) : AndroidViewModel(application) {
    val IdString = MutableLiveData<String>("")
    val PwdString = MutableLiveData<String>("")
    val loginVar=MutableLiveData<Boolean>()
    val joinVar=MutableLiveData<Boolean>()
    val backVar=MutableLiveData<Boolean>()
    val failedLoginToast=MutableLiveData<Boolean>()

    lateinit var loginUser:LoginUserDTO

    fun loginBtnClick(){
        if (IdString.value!!.isEmpty()){
            Toast.makeText(getApplication(),"Id를 입력해주세요",Toast.LENGTH_SHORT).show()
        }
        else if(PwdString.value!!.isEmpty()){
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
                    PostLoginModel(IdString.value.toString(),PwdString.value.toString())
                )

                Log.d("kmj", response.isSuccessful.toString())
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

    fun joinBtnClick(){
        joinVar.value=true
    }

    fun backBtn(){
        backVar.value=true
    }
}