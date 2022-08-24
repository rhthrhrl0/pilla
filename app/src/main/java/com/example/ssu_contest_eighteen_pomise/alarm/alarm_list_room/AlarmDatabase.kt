package com.example.ssu_contest_eighteen_pomise.alarm.alarm_list_room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(AlarmDTO::class), version = 1)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}