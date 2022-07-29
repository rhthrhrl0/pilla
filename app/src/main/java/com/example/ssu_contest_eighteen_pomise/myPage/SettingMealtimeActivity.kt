package com.example.ssu_contest_eighteen_pomise.myPage

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.ssu_contest_eighteen_pomise.App
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
        setMealTime(binding)
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

    private fun setMealTime(binding: ActivitySettingMealtimeBinding) {
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

    fun loadTimePicker(button: Button, timeWhen: String) {

        val shPre = setting_prefs
        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener {
            timePicker, hour, min ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, min)

            button.text = SimpleDateFormat("HH:mm").format(cal.time)
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

        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }
}