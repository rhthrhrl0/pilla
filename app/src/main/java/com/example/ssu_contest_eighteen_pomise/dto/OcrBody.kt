package com.example.ssu_contest_eighteen_pomise.dto

import java.io.Serializable

data class OcrBody(
    val version:String,
    val requestId:String,
    val timestamp:Int,
    val lang:String,
    val images:List<ImagesOcrData>
):Serializable
