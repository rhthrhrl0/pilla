package com.example.ssu_contest_eighteen_pomise.pill_alarm_manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.sharedpreferences.TokenSharedPreferences

class BootReceiver : BroadcastReceiver() {
    private lateinit var shPre: TokenSharedPreferences
    private lateinit var db: RoomDatabase

    override fun onReceive(context: Context, intent: Intent) {
        shPre= App.token_prefs


    }
}