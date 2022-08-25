package com.example.ssu_contest_eighteen_pomise.camera

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.ssu_contest_eighteen_pomise.BuildConfig
import com.example.ssu_contest_eighteen_pomise.camera.add_by_ocr.AddRegisterOcrViewModel
import com.example.ssu_contest_eighteen_pomise.dto.ImagesOcrData
import com.example.ssu_contest_eighteen_pomise.dto.OcrBody
import com.example.ssu_contest_eighteen_pomise.network.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.StringBuilder

class AddPrescriptionViewModel(application: Application) : AndroidViewModel(application) {

    val finishEvent = MutableLiveData<Boolean>()
    val ocrCameraEvent = MutableLiveData<Boolean>()
    val ocrGalleryEvent = MutableLiveData<Boolean>()
    val selfAddPrescriptionEvent = MutableLiveData<Boolean>()
    val failedGetOcrPictureEvent = MutableLiveData<Boolean>()

    private val _eventFlow = MutableSharedFlow<MyEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val ocrRetrofit = Retrofit.Builder()
        .baseUrl(UserService.OCR_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    val ocrService = ocrRetrofit.create(UserService::class.java)

    val successOcrAndImageData = MutableLiveData<AddRegisterOcrViewModel.TransferOcrDTO>()

    fun getOcrData(imageBase64: String, bitmap: Bitmap, isSave: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = ocrService.postOcrImage(
                BuildConfig.OCR_API_SECRET_KEY, "application/json",
                OcrBody(
                    "V2", "temp", 0, "ko", listOf(ImagesOcrData("jpg", imageBase64, "temp"))
                )
            )

            if (response.isSuccessful && response.body()!!.images.elementAt(0).inferResult != "FAILURE") {
                if (isSave) {
                    sendEvent(MyEvent.IsSaveBitmap(bitmap))
                }
                val field = response.body()!!.images.elementAt(0).fields
                val pillList = mutableListOf<AddRegisterOcrViewModel.OcrPillDTO>()
                var i = 0
                for (od in 0..11) {
                    val pillName = deletePillCode(field[i++].inferText.trim())
                    val pillEatCount = field[i++].inferText
                    val pillEatDay = field[i++].inferText
                    val pillEatMethod = field[i++].inferText
                    if (pillName.isNotBlank()) {
                        pillList.add(
                            AddRegisterOcrViewModel.OcrPillDTO(
                                pillName,
                                pillEatCount,
                                pillEatDay,
                                pillEatMethod
                            )
                        )
                    }
                }
                successOcrAndImageData.postValue(AddRegisterOcrViewModel.TransferOcrDTO(pillList))
            } else {
                Log.d("kmj", "실패!!!")
                runBlocking(Dispatchers.Main) {
                    failedGetOcrPictureEvent.value = true
                }
            }
        }
    }

    private fun deletePillCode(str: String): String {
        var temp: Char
        val stringBuilder = StringBuilder()

        if (str.length > 9) {
            var isPrefixAllNum = true
            for (i in 0..8) {
                temp = str.elementAt(i)
                if (temp.code < 48 || temp.code > 57) {
                    isPrefixAllNum = false
                    break
                }
            }
            if (!isPrefixAllNum) {
                return str
            } else {
                for (i in 9 until str.length) {
                    stringBuilder.append(str.elementAt(i))
                }
            }
        } else {
            return str
        }

        if (stringBuilder.toString().trim().isEmpty()) {
            return str
        }

        return stringBuilder.toString().trim()
    }

    fun onClickCameraOCR() {
        ocrCameraEvent.value = true
    }

    fun onClickGalleryOCR() {
        ocrGalleryEvent.value = true
    }

    fun onClickSelfAddAlarm() {
        selfAddPrescriptionEvent.value = true
    }

    fun onClickFinish() {
        finishEvent.value = true
    }

    fun sendEvent(event: MyEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    sealed class MyEvent {
        class IsSaveBitmap(val bitmap: Bitmap) : MyEvent()
    }
}