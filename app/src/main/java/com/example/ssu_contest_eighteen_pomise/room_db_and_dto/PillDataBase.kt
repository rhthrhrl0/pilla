package com.example.ssu_contest_eighteen_pomise.room_db_and_dto

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [RegisteredPill::class],version = 1)
@TypeConverters(RegisteredPill.Companion.TimeConverters::class)
abstract class PillDataBase:RoomDatabase() {
    abstract fun pillDao():PillDao
}