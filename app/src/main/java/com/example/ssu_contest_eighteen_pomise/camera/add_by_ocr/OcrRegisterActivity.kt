package com.example.ssu_contest_eighteen_pomise.camera.add_by_ocr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.dto.OcrAndImageData

class OcrRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocr_register)

        val data=intent.getSerializableExtra(KEY_OCR_DATA_NAME) as OcrAndImageData
        Log.d("kmj","${data.ocrDTO}")
    }

    companion object{
        const val KEY_OCR_DATA_NAME="key_ocr_data_name_"
    }
}