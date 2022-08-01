package com.example.ssu_contest_eighteen_pomise.mainfragments.list

import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.PillNameAndCategory

data class AlarmListDTO(
    var isNextEatPill:Boolean, // 다음 복용 알림 혹은 예정된 알림
    var eatHour:Int,
    var eatMinutes:Int,
    var pillList:MutableList<PillNameAndCategory>
){
    fun copy():AlarmListDTO{
        return AlarmListDTO(isNextEatPill,eatHour,eatMinutes,pillList.toMutableList())
    }
}
