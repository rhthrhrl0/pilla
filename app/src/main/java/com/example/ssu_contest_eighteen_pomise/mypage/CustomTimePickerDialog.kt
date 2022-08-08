package com.example.ssu_contest_eighteen_pomise.mypage

import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.NumberPicker
import android.widget.TimePicker
import java.lang.reflect.Field

//리플렉션을 이용한 방법.. (deprecated)
class CustomTimePickerDialog (
    context: Context?, private val mTimeSetListener: OnTimeSetListener?,
    hourOfDay: Int, minute: Int, is24HourView: Boolean
) :
    TimePickerDialog(
        context, THEME_HOLO_LIGHT, null, hourOfDay,
        minute / CustomTimePickerDialog.Companion.TIME_PICKER_INTERVAL, is24HourView
    ) {
    private var mTimePicker: TimePicker? = null
    override fun updateTime(hourOfDay: Int, minuteOfHour: Int) {
        mTimePicker!!.currentHour = hourOfDay
        mTimePicker!!.currentMinute =
            minuteOfHour / CustomTimePickerDialog.Companion.TIME_PICKER_INTERVAL
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        when (which) {
            BUTTON_POSITIVE -> mTimeSetListener?.onTimeSet(
                mTimePicker, mTimePicker!!.currentHour,
                mTimePicker!!.currentMinute * CustomTimePickerDialog.Companion.TIME_PICKER_INTERVAL
            )
            BUTTON_NEGATIVE -> cancel()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        try {
            val classForid = Class.forName("com.android.internal.R\$id")
            val timePickerField: Field = classForid.getField("timePicker")
            mTimePicker = findViewById(timePickerField.getInt(null)) as TimePicker
            val field: Field = classForid.getField("minute")
            val minuteSpinner = mTimePicker!!.findViewById(field.getInt(null)) as NumberPicker
            minuteSpinner.minValue = 0
            minuteSpinner.maxValue = 60 / CustomTimePickerDialog.Companion.TIME_PICKER_INTERVAL - 1
            val displayedValues: MutableList<String> = ArrayList()
            var i = 0
            while (i < 60) {
                displayedValues.add(String.format("%02d", i))
                i += CustomTimePickerDialog.Companion.TIME_PICKER_INTERVAL
            }
            minuteSpinner.displayedValues = displayedValues
                .toTypedArray()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val TIME_PICKER_INTERVAL = 5
    }
}