package com.example.ssu_contest_eighteen_pomise.mypage

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.dto.UpdatePwDTO
import com.example.ssu_contest_eighteen_pomise.mainfragments.SettingFragmentViewModel
import com.example.ssu_contest_eighteen_pomise.network.LoginService
import com.example.ssu_contest_eighteen_pomise.network.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class UpdatePwdViewModel(application: Application) : AndroidViewModel(application) {
    var oldPwd = ""
    var newPwd1 = ""
    var newPwd2 = ""
    var btn_finish = MutableLiveData<Boolean>()

    fun onOldPwdTextChanged(s: CharSequence, start:Int, before:Int, count:Int) {
        oldPwd = s.toString()
    }

    fun onNewPwd1TextChanged(s: CharSequence, start:Int, before:Int, count:Int) {
        newPwd1 = s.toString()
    }

    fun onNewPwd2TextChanged(s: CharSequence, start:Int, before:Int, count:Int) {
        newPwd2 = s.toString()
    }

    fun updatePwdBtnCLick() {
        Log.d("kyb", oldPwd+","+newPwd1+","+newPwd2)
        val shPre = App.token_prefs
        if(oldPwd.isEmpty())
            Toast.makeText(getApplication(), "기존 비밀번호를 입력해야 합니다.", Toast.LENGTH_SHORT).show()
        else if(newPwd1.isEmpty() || newPwd2.isEmpty())
            Toast.makeText(getApplication(), "변경할 비밀번호를 입력해야 합니다.", Toast.LENGTH_SHORT).show()
        else if(!newPwd1.equals(newPwd2))
            Toast.makeText(getApplication(), "두 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
        else {

            viewModelScope.launch() {
                val response = App.userService.updatePassword(
                    shPre.accessToken!!,
                    UpdatePwDTO(oldPwd, newPwd1)
                )
                if(response.isSuccessful) {
                    Log.d("kyb", "비밀번호 변경 성공?")
                    Toast.makeText(getApplication(), "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
                    btn_finish.value = true
                }
                else {
                    Log.d("kyb", "비밀번호 변경 실패")
                    Toast.makeText(getApplication(), "비밀번호 변경에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun onClickFinish() {
        btn_finish.value = true
    }

}