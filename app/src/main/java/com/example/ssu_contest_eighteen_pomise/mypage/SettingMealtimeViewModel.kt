package com.example.ssu_contest_eighteen_pomise.mypage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.ssu_contest_eighteen_pomise.databinding.ActivitySettingMealtimeBinding
import com.example.ssu_contest_eighteen_pomise.dto.ProtectorDTO
import com.example.ssu_contest_eighteen_pomise.sharedpreferences.SettingSharedPreferences
import java.text.SimpleDateFormat
import java.util.*

class SettingMealtimeViewModel(application: Application) : AndroidViewModel(application) {
    var btn_morning = MutableLiveData<Boolean>()
    var btn_lunch = MutableLiveData<Boolean>()
    var btn_dinner = MutableLiveData<Boolean>()
    var btn_finish = MutableLiveData<Boolean>()
    private var setting_prefs = SettingSharedPreferences

    fun btnMorningClick() {
        btn_morning.value = true
    }

    fun btnLunchClick() {
        btn_lunch.value = true
    }

    fun btnDinnerClick() {
        btn_dinner.value = true
    }

    fun onClickFinish() {
        btn_finish.value = true
    }

    fun setMealTime(binding: ActivitySettingMealtimeBinding) {
        val shPre = setting_prefs
        val cal = Calendar.getInstance()

        cal.set(Calendar.HOUR_OF_DAY, shPre.morningHour!!)
        cal.set(Calendar.MINUTE, shPre.morningMin!!)
        binding.btnMorning.text = SimpleDateFormat("HH:mm").format(cal.time)

        cal.set(Calendar.HOUR_OF_DAY, shPre.lunchHour!!)
        cal.set(Calendar.MINUTE, shPre.lunchMin!!)
        binding.btnLunch.text = SimpleDateFormat("HH:mm").format(cal.time)

        cal.set(Calendar.HOUR_OF_DAY, shPre.dinnerHour!!)
        cal.set(Calendar.MINUTE, shPre.dinnerMin!!)
        binding.btnDinner.text = SimpleDateFormat("HH:mm").format(cal.time)
    }


//
//    private fun getDisplayedValue(
//        @IntRange(from = 0, to = 30)
//        timeInterval: Int = DEFAULT_INTERVAL
//    ): Array<String> {
//        val minutesArray = ArrayList<String>()
//        for (i in 0 until MINUTES_MAX step timeInterval) {
//            minutesArray.add(i.toString())
//        }
//
//        return minutesArray.toArray(arrayOf(""))
//    }
}