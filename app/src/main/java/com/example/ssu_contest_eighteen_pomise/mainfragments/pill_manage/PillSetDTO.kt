package com.example.ssu_contest_eighteen_pomise.mainfragments.pill_manage

import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.SpecificTime
import java.io.Serializable

data class PillSetDTO(
    val createdAt: String,
    val id: MutableList<Int>,
    val pillCategory: String,
    val pillName: String,
    val time: MutableList<SpecificTime>,
    val expireDateYear:Int,
    val expireDateMonth:Int,
    val expireDateDate:Int
):Serializable
