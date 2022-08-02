package com.example.ssu_contest_eighteen_pomise.mypage

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivitySettingMealtimeBinding
import com.example.ssu_contest_eighteen_pomise.sharedpreferences.SettingSharedPreferences
import java.text.SimpleDateFormat
import java.util.*

class SettingMealtimeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingMealtimeBinding
    private lateinit var setting_prefs:SettingSharedPreferences
    private val viewModel: SettingMealtimeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting_mealtime)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        SettingSharedPreferences.setInstance(applicationContext)
        setting_prefs = SettingSharedPreferences
        viewModel.setMealTime(binding)
        val shPre = setting_prefs
        Log.d("kyb", shPre.morningHour.toString()+":"+shPre.morningMin.toString()+" "+shPre.lunchHour.toString()+":"+shPre.lunchMin.toString()+" "+shPre.dinnerHour.toString()+":"+shPre.dinnerMin.toString())

        viewModel.btn_morning.observe(this, {
            loadTimePicker(binding.btnMorning, "morning")
        })

        viewModel.btn_lunch.observe(this, {
            loadTimePicker(binding.btnLunch, "lunch")
        })

        viewModel.btn_dinner.observe(this, {
            loadTimePicker(binding.btnDinner, "dinner")
        })

        viewModel.btn_finish.observe(this, {
            backToPrevPage()
        })

    }

    private fun backToPrevPage() {
        finish()
    }


    fun loadTimePicker(button: Button, timeWhen: String) {

        val shPre = setting_prefs
        val cal = Calendar.getInstance()
        var hour = 0
        var min = 0

        //dialog가 떴을 때 값 받아오기
        if(timeWhen.equals("morning")) {
            hour = setting_prefs.morningHour!!
            min = setting_prefs.morningMin!!
        }
        else if(timeWhen.equals("lunch")) {
            hour = setting_prefs.lunchHour!!
            min = setting_prefs.lunchMin!!
        }
        else if(timeWhen.equals("dinner")) {
            hour = setting_prefs.dinnerHour!!
            min = setting_prefs.dinnerMin!!
        }

        val timeSetListener = TimePickerDialog.OnTimeSetListener {
            _, hour, min ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, min)

            button.text = SimpleDateFormat("HH:mm").format(cal.time) //버튼에 설정시간 보이도록
            if(timeWhen.equals("morning")) {
                shPre.morningHour = hour
                shPre.morningMin = min
            }
            else if(timeWhen.equals("lunch")) {
                shPre.lunchHour = hour
                shPre.lunchMin = min
            }
            else if(timeWhen.equals("dinner")) {
                shPre.dinnerHour = hour
                shPre.dinnerMin = min
            }
        }
        CustomTimePickerDialog(this, timeSetListener, hour, min, false).show()
    }


//    inner class CustomTimePickerDialog(
//        context: Context?,
//        listener: OnTimeSetListener?,
//        hourOfDay: Int,
//        minute: Int,
//        is24HourView: Boolean
//    ) : TimePickerDialog(context, listener, hourOfDay, minute/5, is24HourView) {
//
//        private lateinit var mTimePicker:TimePicker
//
//        override fun updateTime(hourOfDay: Int, minuteOfHour: Int) {
//            mTimePicker.currentHour = hourOfDay
//            mTimePicker.currentMinute = minuteOfHour/5
//        }
//    }
}