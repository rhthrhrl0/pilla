package com.example.ssu_contest_eighteen_pomise.dto

import java.io.Serializable

data class Field(
    val boundingPoly: BoundingPoly,
    val inferConfidence: Double,
    val inferText: String,
    val name: String,
    val subFields: List<SubField>,
    val type: String,
    val valueType: String
):Serializable