package com.example.ssu_contest_eighteen_pomise.room_db_and_dto

import androidx.room.TypeConverter
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.SpecificTime
import com.google.gson.Gson

data class RegisteredPill(
    var pillName:String,
    var pillCategory:String,
    var eatTime:List<SpecificTime>,
    var dateYear:Int,
    var dateMonth:Int,
    var dateDay:Int
) {

    companion object {
        class TimeConverters {
            // 저장할때는 Json의 스트링형식으로
            @TypeConverter
            fun listToJson(value: List<SpecificTime>?) = Gson().toJson(value)

            // 꺼낼때는 리스트로 다시 바꿈.
            @TypeConverter
            fun jsonToList(value: String) =
                Gson().fromJson(value, Array<SpecificTime>::class.java).toList()
        }
    }
}

