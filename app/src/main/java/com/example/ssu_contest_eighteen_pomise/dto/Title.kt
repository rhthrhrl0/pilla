package com.example.ssu_contest_eighteen_pomise.dto

import java.io.Serializable

data class Title(
    val boundingPoly: BoundingPoly,
    val inferConfidence: Double,
    val inferText: String,
    val name: String,
    val subFields: List<SubField>
):Serializable