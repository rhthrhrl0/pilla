package com.example.ssu_contest_eighteen_pomise.room_db_and_dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.SpecificTime
import com.google.gson.Gson

@Entity
data class Protectors(
    var name:String,
    var description:String,
    var phoneNum:String,
    var email:String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}