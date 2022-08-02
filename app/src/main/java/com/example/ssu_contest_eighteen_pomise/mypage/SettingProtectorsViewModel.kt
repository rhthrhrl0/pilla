package com.example.ssu_contest_eighteen_pomise.mypage

import android.annotation.SuppressLint
import android.app.Application
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.annotation.IntRange
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.ssu_contest_eighteen_pomise.dto.ProtectorDTO
import java.util.ArrayList

class SettingProtectorsViewModel(application: Application) : AndroidViewModel(application) {

    var btn_finish = MutableLiveData<Boolean>()
    val protectorsList = MutableLiveData<MutableList<ProtectorDTO>>()
//    val protectorsList = arrayListOf(
//        ProtectorDTO("name", "description", "phoneNum", "email"),
//        ProtectorDTO("name2", "description2", "phoneNum2", "email2")
//    )

    fun onClickFinish() {
        btn_finish.value = true
    }

}