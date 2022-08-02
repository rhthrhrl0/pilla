package com.example.ssu_contest_eighteen_pomise.room_db_and_dto

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Protectors::class], version = 1)
abstract class ProtectorDataBase:RoomDatabase() {
    abstract fun protectorDAO():ProtectorDAO
}