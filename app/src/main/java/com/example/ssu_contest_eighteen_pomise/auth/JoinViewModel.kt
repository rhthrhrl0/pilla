package com.example.ssu_contest_eighteen_pomise.auth

import android.app.Application
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ssu_contest_eighteen_pomise.dto.PostSignUpModel
import com.example.ssu_contest_eighteen_pomise.dto.SignUpDTO
import com.example.ssu_contest_eighteen_pomise.network.LoginService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class JoinViewModel(application: Application) : AndroidViewModel(application) {
    val backBtn = MutableLiveData<Boolean>()
    val joinBtn = MutableLiveData<Boolean>()
    val failedJoinToast=MutableLiveData<Boolean>()
    val nameString=MutableLiveData<String>("")
    val idString = MutableLiveData<String>("")
    val pwd_1_string = MutableLiveData<String>("")
    val pwd_2_string = MutableLiveData<String>("")

    lateinit var signUpUser:SignUpDTO

    fun backBtnClick() {
        backBtn.value = true
    }

    fun joinBtnClick() {
        val name=nameString.value.toString()
        val id = idString.value.toString()
        val pwd1 = pwd_1_string.value.toString()
        val pwd2 = pwd_2_string.value.toString()
        when {
            name.isEmpty()->{
                Toast.makeText(getApplication(), "이름을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            id.isEmpty() -> {
                Toast.makeText(getApplication(), "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            pwd1.isEmpty() -> {
                Toast.makeText(getApplication(), "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            pwd2.isEmpty() -> {
                Toast.makeText(getApplication(),"비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            pwd1!=pwd2 -> {
                Toast.makeText(getApplication(),"비밀번호가 다릅니다.",Toast.LENGTH_SHORT).show()
            }
            else -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val retrofit = Retrofit.Builder()
                        .baseUrl(LoginService.BASE_URL)
                        .addConverterFactory(MoshiConverterFactory.create())
                        .build()

                    //레트로핏 사용할 준비완료.
                    val service = retrofit.create(LoginService::class.java)
                    val response=service.signUpRequest(
                        PostSignUpModel(id,pwd1,LoginService.USER,name)
                    )

                    Log.d("kmj", response.isSuccessful.toString())
                    if (response.isSuccessful){
                        signUpUser= response.body()!!
                        joinBtn.postValue(true)
                    }
                    else{
                        failedJoinToast.postValue(true)
                    }
                }
            }
        }
    }
}