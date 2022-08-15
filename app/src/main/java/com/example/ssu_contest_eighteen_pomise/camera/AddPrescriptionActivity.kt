package com.example.ssu_contest_eighteen_pomise.camera

import android.app.ProgressDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.auth.JoinActivity
import com.example.ssu_contest_eighteen_pomise.camera.add_by_ocr.OcrRegisterActivity
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.AddSelfNoOcrActivity
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityAddPrescriptionBinding
import com.example.ssu_contest_eighteen_pomise.extensionfunction.slideNoneAndDownExit
import java.io.ByteArrayOutputStream


class AddPrescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPrescriptionBinding
    private val viewModel: AddPrescriptionViewModel by viewModels()
    private lateinit var dialog:ProgressDialog
    private lateinit var prescriptionImageBitmap: Bitmap
    private val filterActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK && it.data != null) {
                var currentImageUri = it.data?.data
                try {
                    Log.d("kmj", "원본: " + currentImageUri)
                    currentImageUri?.let {
                        if (Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                this.contentResolver,
                                currentImageUri
                            )
                            prescriptionImageBitmap = bitmap
                        } else {
                            val source =
                                ImageDecoder.createSource(this.contentResolver, currentImageUri)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            prescriptionImageBitmap = bitmap
                        }
                        //액티비티 실행 코드
                        Log.d("kmj", "" + prescriptionImageBitmap)

                        val byteArrayOutputStream = ByteArrayOutputStream()

                        //해당 비트맵 크기를 줄이기 위해 압축해서 인자로 넘겨준 바이트배열아웃풋스트림으로 받음.
                        prescriptionImageBitmap.compress(
                            Bitmap.CompressFormat.JPEG,
                            99,
                            byteArrayOutputStream
                        )
                        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
                        val outStream = ByteArrayOutputStream()
                        val res: Resources = resources
                        val ImageBase64 = Base64.encodeToString(byteArray, 0)
                        dialog.show()
                        viewModel.getOcrData(ImageBase64)
                        //이걸 일단 OCR 보내보기. 그리고 결과보고 받을 수 있으면 액티비티 실행.
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (it.resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show()
            } else {
                Log.d("ActivityResult", "something wrong")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_prescription)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        dialog = ProgressDialog(this)
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setTitle("이미지 분석중")
        dialog.setMessage("잠시만 기다려주세요")
        dialog.setCancelable(false)


        viewModel.successOcrAndImageData.observe(this@AddPrescriptionActivity, {
            val intent = Intent(this, OcrRegisterActivity::class.java)
            intent.putExtra(
                OcrRegisterActivity.KEY_OCR_DATA_NAME,
                it
            )
            startActivity(intent)
            dialog.dismiss()
        })

        viewModel.finishEvent.observe(this@AddPrescriptionActivity, {
            onBackPressed()
        })

        viewModel.ocrEvent.observe(this@AddPrescriptionActivity, {
            onStartGallery()
        })

        viewModel.failedGetOcrPictureEvent.observe(this@AddPrescriptionActivity, {
            dialog.dismiss()
        })

        viewModel.selfAddPrescriptionEvent.observe(this@AddPrescriptionActivity, {
            onStartSelfAddAlarm()
        })

    }

    fun onStartCamera() {

    }

    fun onStartGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        filterActivityLauncher.launch(intent)
    }

    fun onStartSelfAddAlarm() {
        val intent = Intent(this, AddSelfNoOcrActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        slideNoneAndDownExit()
    }

    private fun requestPermissions() {
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            Log.d("권한요청", "$it")
        }.launch(PERMISSIONS_REQUESTED)
    }

    companion object {
        private const val PERMISSION_CAMERA = android.Manifest.permission.CAMERA
        private const val PERMISSION_READ_EXTERNAL_STORAGE =
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        private const val PERMISSION_WRITE_EXTERNAL_STORAGE =
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE

        private val PERMISSIONS_REQUESTED: Array<String> = arrayOf(
            PERMISSION_CAMERA,
            PERMISSION_READ_EXTERNAL_STORAGE,
            PERMISSION_WRITE_EXTERNAL_STORAGE,
        )
    }
}