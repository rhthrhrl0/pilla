package com.example.ssu_contest_eighteen_pomise.room_db_and_dto

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProtectorDAO {
    @Query("SELECT * FROM Protectors")
    suspend fun getAll(): List<Protectors>

    @Insert
    suspend fun insert(protector: Protectors)

    @Delete
    suspend fun delete(protector: Protectors)

    @Update
    suspend fun update(protector: Protectors)
}