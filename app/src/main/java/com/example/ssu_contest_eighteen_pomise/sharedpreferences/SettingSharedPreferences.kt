package com.example.ssu_contest_eighteen_pomise.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.example.ssu_contest_eighteen_pomise.App

object SettingSharedPreferences {

    private val shPre = App.token_prefs
    private lateinit var prefs: SharedPreferences
    private val alarm_sound = "sound_mode_"
    private val alarm_vibrate = "vibrate_mode_"
    private val key_morningHour = "morningHour"
    private val key_morningMin = "morningMin"
    private val key_lunchHour = "lunchHour"
    private val key_lunchMin = "lunchMin"
    private val key_dinnerHour = "dinnerHour"
    private val key_dinnerMin = "dinnerMin"
    private val key_birthYear="birthYear"
    private val key_birthMonth="birthMonth"
    private val key_birthDay="birthDay"

    fun setInstance(context: Context):SettingSharedPreferences {
        prefs = context.getSharedPreferences("shpre-${shPre.email}", 0)
        return this
    }

    var sound: String?
        get() = prefs.getString(alarm_sound, "on")
        set(value) = prefs.edit().putString(alarm_sound, value).apply()
    var vibrate: String?
        get() = prefs.getString(alarm_vibrate, "on")
        set(value) = prefs.edit().putString(alarm_vibrate, value).apply()
    var morningHour: Int?
        get() = prefs.getInt(key_morningHour, 7)
        set(value) = prefs.edit().putInt(key_morningHour, value!!).apply()
    var morningMin: Int?
        get() = prefs.getInt(key_morningMin, 30)
        set(value) = prefs.edit().putInt(key_morningMin, value!!).apply()
    var lunchHour: Int?
        get() = prefs.getInt(key_lunchHour, 12)
        set(value) = prefs.edit().putInt(key_lunchHour, value!!).apply()
    var lunchMin: Int?
        get() = prefs.getInt(key_lunchMin, 0)
        set(value) = prefs.edit().putInt(key_lunchMin, value!!).apply()
    var dinnerHour: Int?
        get() = prefs.getInt(key_dinnerHour, 17)
        set(value) = prefs.edit().putInt(key_dinnerHour, value!!).apply()
    var dinnerMin: Int?
        get() = prefs.getInt(key_dinnerMin, 30)
        set(value) = prefs.edit().putInt(key_dinnerMin, value!!).apply()

    var birthYear:Int?
        get() = prefs.getInt(key_birthYear,0)
        set(value) = prefs.edit().putInt(key_birthYear,value!!).apply()

    var birthMonth:Int?
        get() = prefs.getInt(key_birthMonth,0)
        set(value) = prefs.edit().putInt(key_birthMonth,value!!).apply()

    var birthDay:Int?
        get() = prefs.getInt(key_birthDay,0)
        set(value) = prefs.edit().putInt(key_birthDay,value!!).apply()
}