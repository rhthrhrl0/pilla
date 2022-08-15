package com.example.ssu_contest_eighteen_pomise.dto

import java.io.Serializable

data class SubField(
    val boundingPoly: BoundingPoly,
    val inferConfidence: Double,
    val inferText: String,
    val lineBreak: Boolean
):Serializable