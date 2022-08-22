package com.example.ssu_contest_eighteen_pomise.auth

import android.app.Application
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.dto.PostSignUpModel
import com.example.ssu_contest_eighteen_pomise.dto.SignUpDTO
import com.example.ssu_contest_eighteen_pomise.network.LoginService
import com.example.ssu_contest_eighteen_pomise.network.LoginService.Companion.getUnsafeOkHttpClient
import com.yourssu.design.system.atom.Toggle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class JoinViewModel(application: Application) : AndroidViewModel(application) {
    val backBtn = MutableLiveData<Boolean>()
    val joinBtn = MutableLiveData<Boolean>()
    val failedJoinToast = MutableLiveData<Boolean>()
    var nameString = ""
    var idString = ""
    var pwd_1_string = ""
    var pwd_2_string = ""
    val isGuardian = MutableLiveData<Boolean>(false) // 기본은 환자로.
    var phoneNumberString = ""
    val isJoinClickabled = MutableLiveData<Boolean>(false)

    val toggleSelectedListener = object : Toggle.SelectedListener{
        override fun onSelected(boolean: Boolean) {
            isGuardian.value = boolean
        }
    }

    lateinit var signUpUser: SignUpDTO

    fun onPillNameTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        nameString = s.toString()
        isCanPillAdd()
    }
    fun onPillPhoneNumberTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        phoneNumberString = s.toString()
        isCanPillAdd()
    }
    fun onPillIdTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        idString = s.toString()
        isCanPillAdd()
    }
    fun onPillPwdTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        pwd_1_string = s.toString()
        isCanPillAdd()
    }
    fun onPillRePwdTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        pwd_2_string = s.toString()
        isCanPillAdd()
    }

    fun backBtnClick() {
        backBtn.value = true
    }

    fun joinBtnClick() {
        val name = nameString
        val id = idString
        val pwd1 = pwd_1_string
        val pwd2 = pwd_2_string
        val phoneNumber = phoneNumberString

        when {
            name.isEmpty() -> {
                Toast.makeText(getApplication(), "이름을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            phoneNumber.isEmpty() -> {
                Toast.makeText(getApplication(), "휴대폰 번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            !chkNum(phoneNumber)->{
                Toast.makeText(getApplication(), "휴대폰 번호를 '-'를 제외하고\n숫자만 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            id.isEmpty() -> {
                Toast.makeText(getApplication(), "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            pwd1.isEmpty() -> {
                Toast.makeText(getApplication(), "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            pwd2.isEmpty() -> {
                Toast.makeText(getApplication(), "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            pwd1 != pwd2 -> {
                Toast.makeText(getApplication(), "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                viewModelScope.launch(Dispatchers.IO) {
                    //레트로핏 사용할 준비완료.
                    val response = App.loginService.signUpRequest(
                        PostSignUpModel(
                            id,
                            isGuardian.value!!,
                            pwd1,
                            phoneNumber,
                            LoginService.USER,
                            name
                        )
                    )

                    Log.d("kmj", response.isSuccessful.toString())
                    if (response.isSuccessful) {
                        signUpUser = response.body()!!
                        joinBtn.postValue(true)
                    } else {
                        Log.d("kmj", "회원가입 실패:"+response)
                        failedJoinToast.postValue(true)
                    }
                }
            }
        }
    }

    fun isCanPillAdd() {
        isJoinClickabled.value =
            (nameString.isNotEmpty() && idString.isNotEmpty() && pwd_1_string.isNotEmpty() && pwd_2_string.isNotEmpty() && phoneNumberString.isNotEmpty())
    }

    fun chkNum(str: String) : Boolean {
        var temp: Char

        var result = true

        for (i in 0 until str.length) {
            temp = str.elementAt(i)
            if (temp.toInt() < 48 || temp.toInt() > 57) {
                result = false
            }
        }

        return result
    }
}