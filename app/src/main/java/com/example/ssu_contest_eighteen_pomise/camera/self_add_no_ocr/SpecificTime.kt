package com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr

import java.io.Serializable

data class SpecificTime(
    var hour:Int,
    var minutes:Int,
    var sec:Int=0
):Serializable
