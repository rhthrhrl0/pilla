package com.example.ssu_contest_eighteen_pomise.dto

import java.io.Serializable

data class OcrDTO(
    val images: List<OcrImage>,
    val requestId: String,
    val timestamp: Long,
    val version: String
):Serializable