package com.example.ssu_contest_eighteen_pomise

import android.app.Activity
import android.app.Application
import com.example.ssu_contest_eighteen_pomise.sharedpreferences.TokenSharedPreferences

class App:Application() {
    companion object{
        lateinit var token_prefs:TokenSharedPreferences
    }

    override fun onCreate() {
        token_prefs=TokenSharedPreferences(applicationContext) //초기화.
        super.onCreate()
    }
}