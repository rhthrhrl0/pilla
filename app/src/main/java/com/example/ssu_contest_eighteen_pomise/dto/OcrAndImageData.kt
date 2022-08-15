package com.example.ssu_contest_eighteen_pomise.dto

import java.io.Serializable

data class OcrAndImageData(
    val ocrDTO: OcrDTO,
    val imageBase64:String
):Serializable
