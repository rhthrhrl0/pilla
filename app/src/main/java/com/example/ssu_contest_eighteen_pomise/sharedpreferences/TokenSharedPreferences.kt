package com.example.ssu_contest_eighteen_pomise.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

class TokenSharedPreferences(context:Context) {
    private val prefsFilename = "token_prefs"
    private val key_accessToken = "accessToken"
    private val key_refreshToken = "refreshToken"
    private val key_email="email"
    private val key_name="name"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename,0)

    var accessToken: String?
        get() = prefs.getString(key_accessToken,"")
        set(value) = prefs.edit().putString(key_accessToken,value).apply()
    var refreshToken: String?
        get() = prefs.getString(key_refreshToken,"")
        set(value) = prefs.edit().putString(key_refreshToken,value).apply()
    var email:String?
        get()=prefs.getString(key_email,"")
        set(value)=prefs.edit().putString(key_email,value).apply()
    var name:String?
        get()=prefs.getString(key_name,"")
        set(value)=prefs.edit().putString(key_name,value).apply()
}