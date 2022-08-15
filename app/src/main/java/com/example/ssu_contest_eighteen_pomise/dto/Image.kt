package com.example.ssu_contest_eighteen_pomise.dto

import java.io.Serializable

data class OcrImage(
    val fields: List<Field>,
    val inferResult: String,
    val matchedTemplate: MatchedTemplate,
    val message: String,
    val name: String,
    val title: Title,
    val uid: String,
    val validationResult: ValidationResult
):Serializable