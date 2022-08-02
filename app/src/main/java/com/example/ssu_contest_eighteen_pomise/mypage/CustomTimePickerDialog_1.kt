package com.example.ssu_contest_eighteen_pomise.mypage

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.annotation.IntRange

class CustomTimePickerDialog_1(
    context: Context?,
    listener: OnTimeSetListener?,
    hourOfDay: Int,
    minute: Int,
    is24HourView: Boolean
) : TimePickerDialog(context, listener, hourOfDay, minute/5, is24HourView) {

    init {
    }

    @SuppressLint("PrivateApi")
    fun TimePicker.setTimeInterval(
        @IntRange(from = 0, to = 30)
        timeInterval: Int = 5
    ) {
        try {
            val classForId = Class.forName("com.android.internal.R\$id")
            val fieldId = classForId.getField("minute").getInt(null)

            (this.findViewById(fieldId) as NumberPicker).apply {
                minValue = MINUTES_MIN
                maxValue = MINUTES_MAX / timeInterval-1
                displayedValues = getDisplayedValue(timeInterval)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getDisplayedValue(
        @IntRange(from = 0, to = 30)
        timeInterval: Int = DEFAULT_INTERVAL
    ): Array<String> {
        val minutesArray = ArrayList<String>()
        for (i in 0 until 60 step timeInterval) {
            minutesArray.add(i.toString())
        }

        return minutesArray.toArray(arrayOf(""))
    }

    fun TimePicker.getDisplayedMinute(
        @IntRange(from = 0, to = 30)
        timeInterval: Int = DEFAULT_INTERVAL
    ): Int = minute * timeInterval


    companion object {
        const val DEFAULT_INTERVAL = 5
        const val MINUTES_MIN = 0
        const val MINUTES_MAX = 60
    }
}