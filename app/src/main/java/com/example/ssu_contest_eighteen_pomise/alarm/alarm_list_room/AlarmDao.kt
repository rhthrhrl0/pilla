package com.example.ssu_contest_eighteen_pomise.alarm.alarm_list_room

import androidx.room.*

@Dao
interface AlarmDao {
    @Query("SELECT * FROM AlarmDTO")
    suspend fun getAll(): List<AlarmDTO>

    @Insert
    suspend fun insert(alarmDTO:AlarmDTO)

    @Delete
    suspend fun delete(alarmDTO:AlarmDTO)

    @Update
    suspend fun update(alarmDTO:AlarmDTO)
}