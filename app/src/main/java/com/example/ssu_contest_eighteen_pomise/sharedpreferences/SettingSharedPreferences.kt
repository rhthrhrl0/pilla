package com.example.ssu_contest_eighteen_pomise.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.example.ssu_contest_eighteen_pomise.App

object SettingSharedPreferences {

    private val shPre = App.token_prefs
    private lateinit var prefs: SharedPreferences
    private val alarm_sound = "soundOn"
    private val alarm_vibrate = "vibrateOn"
    private val key_morningHour = "morningHour"
    private val key_morningMin = "morningMin"
    private val key_lunchHour = "lunchHour"
    private val key_lunchMin = "lunchMin"
    private val key_dinnerHour = "dinnerHour"
    private val key_dinnerMin = "dinnerMin"

    fun setInstance(context: Context):SettingSharedPreferences {
        prefs = context.getSharedPreferences("shpre-${shPre.email}", 0)
        return this
    }

    var sound: String?
        get() = prefs.getString(alarm_sound, "off")
        set(value) = prefs.edit().putString(alarm_sound, value).apply()
    var vibrate: String?
        get() = prefs.getString(alarm_vibrate, "off")
        set(value) = prefs.edit().putString(alarm_vibrate, value).apply()
    var morningHour: Int?
        get() = prefs.getInt(key_morningHour, 0)
        set(value) = prefs.edit().putInt(key_morningHour, value!!).apply()
    var morningMin: Int?
        get() = prefs.getInt(key_morningMin, 0)
        set(value) = prefs.edit().putInt(key_morningMin, value!!).apply()
    var lunchHour: Int?
        get() = prefs.getInt(key_lunchHour, 0)
        set(value) = prefs.edit().putInt(key_lunchHour, value!!).apply()
    var lunchMin: Int?
        get() = prefs.getInt(key_lunchMin, 0)
        set(value) = prefs.edit().putInt(key_lunchMin, value!!).apply()
    var dinnerHour: Int?
        get() = prefs.getInt(key_dinnerHour, 0)
        set(value) = prefs.edit().putInt(key_dinnerHour, value!!).apply()
    var dinnerMin: Int?
        get() = prefs.getInt(key_dinnerMin, 0)
        set(value) = prefs.edit().putInt(key_dinnerMin, value!!).apply()

}