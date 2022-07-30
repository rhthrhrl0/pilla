package com.example.ssu_contest_eighteen_pomise.room_db_and_dto

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PillDao {
    @Query("SELECT * FROM RegisteredPill")
    suspend fun getAll(): List<RegisteredPill>

    @Insert
    suspend fun insert(pill: RegisteredPill)

    @Delete
    suspend fun delete(pill: RegisteredPill)

    @Update
    suspend fun update(pill: RegisteredPill)
}