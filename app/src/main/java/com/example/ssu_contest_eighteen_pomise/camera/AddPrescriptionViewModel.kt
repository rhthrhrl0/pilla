package com.example.ssu_contest_eighteen_pomise.camera

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import com.example.ssu_contest_eighteen_pomise.BuildConfig
import com.example.ssu_contest_eighteen_pomise.dto.ImagesOcrData
import com.example.ssu_contest_eighteen_pomise.dto.OcrAndImageData
import com.example.ssu_contest_eighteen_pomise.dto.OcrBody
import com.example.ssu_contest_eighteen_pomise.network.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class AddPrescriptionViewModel(application: Application) : AndroidViewModel(application) {

    val finishEvent = MutableLiveData<Boolean>()
    val ocrEvent = MutableLiveData<Boolean>()
    val selfAddPrescriptionEvent = MutableLiveData<Boolean>()
    val failedGetOcrPictureEvent=MutableLiveData<Boolean>()

    val userRetrofit = Retrofit.Builder()
        .baseUrl(UserService.OCR_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    val ocrService = userRetrofit.create(UserService::class.java)

    val successOcrAndImageData=MutableLiveData<OcrAndImageData>()

    fun getOcrData(imageBase64: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = ocrService.postOcrImage(
                BuildConfig.OCR_API_SECRET_KEY, "application/json",
                OcrBody(
                    "V2", "temp", 0, "ko", listOf(ImagesOcrData("jpg", imageBase64, "temp"))
                )
            )

            if (response.isSuccessful && response.body()!!.images.elementAt(0).inferResult!="FAILURE") {
                Log.d("kmj", "성공 ${response.body()!!.images.elementAt(0).fields.elementAt(0).inferText}")
                successOcrAndImageData.postValue(OcrAndImageData(response.body()!!,imageBase64))
            } else {
                runBlocking(Dispatchers.Main) {
                    failedGetOcrPictureEvent.value=true
                    Toast.makeText(getApplication(),"지원하는 양식의 처방전이 아닙니다",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun onClickOCR() {
        ocrEvent.value = true
    }

    fun onClickSelfAddAlarm() {
        selfAddPrescriptionEvent.value = true
    }

    fun onClickFinish() {
        finishEvent.value = true
    }
}