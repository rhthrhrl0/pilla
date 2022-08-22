package com.example.ssu_contest_eighteen_pomise.camera.add_by_ocr

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.AddSelfNoOcrActivity
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.PillAddErrorType
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.PillNameAndCategory
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.SpecificTime
import com.example.ssu_contest_eighteen_pomise.dto.OcrAndImageData
import com.example.ssu_contest_eighteen_pomise.network.LoginService
import com.example.ssu_contest_eighteen_pomise.room_db_and_dto.RegisteredPill
import com.example.ssu_contest_eighteen_pomise.sharedpreferences.SettingSharedPreferences
import com.yourssu.design.system.atom.Checkbox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.CountDownLatch

class AddRegisterOcrViewModel(application: Application) : AndroidViewModel(application) {
    private val shPre = App.token_prefs
    private val settShrpe = SettingSharedPreferences.setInstance(application)

    lateinit var ocrImageDataInit: OcrAndImageData
    var pillSetList = MutableLiveData<MutableList<OcrRegisterDTO>>()
    var curIndex = -1
        set(value) {
            if (field != value) {
                field = value
                changeIndex(value)
            }
        }

    val size: MutableLiveData<Int> = MutableLiveData(Checkbox.SMALL)
    val finishEvent = MutableLiveData<Boolean>()
    val addPrescriptionEvent = MutableLiveData<Boolean>()

    val isSelect1: MutableLiveData<Boolean> = MutableLiveData(true)
    val isSelect2: MutableLiveData<Boolean> = MutableLiveData(false)
    val isSelect3: MutableLiveData<Boolean> = MutableLiveData(false)

    var startTimeInt: Int = 12
        set(value) {
            field = value
            startTimeString.value = ("$field")
            pillSetList.value?.elementAt(curIndex)?.startTimeInt = value
        }
    val startTimeString = MutableLiveData<String>("12")
    var cycleTimeInt: Int = 0
        set(value) {
            field = value
            cycleTimeString.value = ("$field")
            pillSetList.value?.elementAt(curIndex)?.cycleTimeInt = value
        }
    val cycleTimeString = MutableLiveData<String>("0")

    var morningEatTime: String = "복용 안함"
        set(value) {
            field = value
            morningEatTimeString.value = value
            pillSetList.value?.elementAt(curIndex)?.morningEatTimeString = value
        }
    var lunchEatTime: String = "복용 안함"
        set(value) {
            field = value
            lunchEatTimeString.value = value
            pillSetList.value?.elementAt(curIndex)?.lunchEatTimeString = value
        }
    var dinnerEatTime: String = "복용 안함"
        set(value) {
            field = value
            dinnerEatTimeString.value = value
            pillSetList.value?.elementAt(curIndex)?.dinnerEatTimeString = value
        }
    val morningEatTimeString = MutableLiveData("복용 안함")
    val lunchEatTimeString = MutableLiveData("복용 안함")
    val dinnerEatTimeString = MutableLiveData("복용 안함")

    var specificTimeHourInt: Int = 12
        set(value) {
            field = value
            specificTimeHourString.value = "$field"
        }
    val specificTimeHourString = MutableLiveData<String>("12")
    var specificTimeMinutesInt: Int = 30
        set(value) {
            field = value
            specificTimeMinutesString.value = "$field"
        }
    val specificTimeMinutesString = MutableLiveData<String>("30")

    val specificTimeItemLiveData = MutableLiveData<MutableList<SpecificTime>>()


    val pillNameCategoryListLiveData = MutableLiveData<MutableList<PillNameAndCategory>>()
    val isPillAddButtonIsDisabled = MutableLiveData(true)
    var pillNameString: String = ""
    fun onPillNameTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        pillNameString = s.toString()
        isCanPillAdd()
    }

    var pillCategoryInt: Int = 0
        set(value) {
            field = value
            pillCategoryString.value = AddSelfNoOcrActivity.pillCategoryList[value]
            isCanPillAdd()
        }
    var pillCategoryString = MutableLiveData<String>("선택안함")

    var expirationYearInt: Int = 0
        set(value) {
            if (value > 0) {
                field = value
                expirationYear.value = "$field"
                pillSetList.value?.elementAt(curIndex)?.expirationYearInt = value
            } else {
                field = value
                expirationYear.value = ""
                pillSetList.value?.elementAt(curIndex)?.expirationYearInt = 0
            }
        }
    var expirationMonthInt: Int = 0
        set(value) {
            if (value > 0) {
                field = value
                expirationMonth.value = "$field"
                pillSetList.value?.elementAt(curIndex)?.expirationMonthInt = value
            } else {
                field = 0
                expirationMonth.value = ""
                pillSetList.value?.elementAt(curIndex)?.expirationMonthInt = 0
            }
        }
    var expirationDayInt: Int = 0
        set(value) {
            if (value > 0) {
                field = value
                expirationDay.value = "$field"
                pillSetList.value?.elementAt(curIndex)?.expirationDayInt = value
            } else {
                field = value
                expirationDay.value = ""
                pillSetList.value?.elementAt(curIndex)?.expirationDayInt = 0
            }
        }
    val expirationYear = MutableLiveData<String>("")
    val expirationMonth = MutableLiveData<String>("")
    val expirationDay = MutableLiveData<String>("")

    private fun isCanPillAdd() {
        isPillAddButtonIsDisabled.value =
            pillNameString.isBlank() || pillCategoryInt == 0
    }

    val selectedStateListener1 = object : Checkbox.SelectedListener {
        override fun onSelected(boolean: Boolean) {
            isSelect1.value = boolean
            if (boolean)
                resetExceptTrue(1)
        }
    }

    val selectedStateListener2 = object : Checkbox.SelectedListener {
        override fun onSelected(boolean: Boolean) {
            isSelect2.value = boolean
            if (boolean)
                resetExceptTrue(2)
        }
    }

    val selectedStateListener3 = object : Checkbox.SelectedListener {
        override fun onSelected(boolean: Boolean) {
            isSelect3.value = boolean
            if (boolean)
                resetExceptTrue(3)
        }
    }

    fun resetExceptTrue(except: Int) {
        when (except) {
            1 -> {
                isSelect1.value = true
                isSelect2.value = false
                isSelect3.value = false
            }
            2 -> {
                isSelect1.value = false
                isSelect2.value = true
                isSelect3.value = false
            }
            3 -> {
                isSelect1.value = false
                isSelect2.value = false
                isSelect3.value = true
            }
        }
        pillSetList.value?.elementAt(curIndex)?.isSelect1Value = isSelect1.value!!
        pillSetList.value?.elementAt(curIndex)?.isSelect2Value = isSelect2.value!!
        pillSetList.value?.elementAt(curIndex)?.isSelect3Value = isSelect3.value!!
    }

    fun addSpecificTimeItem() {
        specificTimeItemLiveData.value?.add(
            SpecificTime(
                specificTimeHourInt,
                specificTimeMinutesInt,
                0
            )
        )
        val newArray =
            specificTimeItemLiveData.value?.sortedWith(compareBy({ it.hour }, { it.minutes }))
        val newSet = newArray?.toMutableSet()
        // 관찰 당하고 있으므로 어댑터 쪽 업데이트 할거임.
        if (newSet != null) {
            specificTimeItemLiveData.value = newSet.toMutableList()
            pillSetList.value?.elementAt(curIndex)?.specificTimeItemList =
                specificTimeItemLiveData.value!!
        }
    }

    fun specificTimeItemOnClick(position: Int) {
        if (isSelect3.value!!) {
            specificTimeItemLiveData.value?.removeAt(position)
            specificTimeItemLiveData.value = specificTimeItemLiveData.value
            pillSetList.value?.elementAt(curIndex)?.specificTimeItemList =
                specificTimeItemLiveData.value!!
        }
    }

    fun addPillNameCategoryItem() {
        pillNameCategoryListLiveData.value?.add(
            PillNameAndCategory(
                pillNameString,
                pillCategoryString.value!!
            )
        )
        val newSet = pillNameCategoryListLiveData.value?.toMutableSet()
        if (newSet != null) {
            Log.d("kmj", "${newSet.toMutableList()}")
            pillNameCategoryListLiveData.value = newSet.toMutableList()
            pillSetList.value?.elementAt(curIndex)?.pillNameCategoryList =
                pillNameCategoryListLiveData.value!!
        }
    }

    fun pillNameCategoryOnDeleteClick(position: Int) {
        pillNameCategoryListLiveData.value?.removeAt(position)
        pillNameCategoryListLiveData.value = pillNameCategoryListLiveData.value
        pillSetList.value?.elementAt(curIndex)?.pillNameCategoryList =
            pillNameCategoryListLiveData.value!!
    }

    fun onClickAdd() {
        //
    }

    fun insert(pillAddContentList: List<RegisteredPill>) {
        //뷰모델에서 코루틴 사용할때는 viewModelScope를 사용함.
        val job = viewModelScope.launch(Dispatchers.IO) {
            val response = App.loginService.registerPillRequest(shPre.refreshToken!!, pillAddContentList)
            if (response.isSuccessful) {
                runBlocking(Dispatchers.Main) {
                    addPrescriptionEvent.value = true
                    onClickFinish()
                }
            } else {
                //addErrorString.postValue(PillAddErrorType.REGISTER_ERROR)
            }
        }
    }

    fun classifySet() {
        val field = ocrImageDataInit.ocrDTO.images.elementAt(0).fields
        val pillList = mutableListOf<OcrPillDTO>()
        val tempSet = mutableSetOf<OcrSetDTO>()
        val tempOcrRegisterList = mutableListOf<OcrRegisterDTO>()
        var i = 0

        for (od in 0..11) {
            val pillName = field[i++].inferText
            val pillEatCount = field[i++].inferText
            val pillEatDay = field[i++].inferText
            val pillEatMethod = field[i++].inferText
            if (pillName.isNotBlank()) {
                pillList.add(OcrPillDTO(pillName, pillEatCount, pillEatDay, pillEatMethod))
            }
        }

        for (p in pillList) {
            tempSet.add(OcrSetDTO(p.pillEatCount, p.pillEatDay, p.pillEatMethod))
        }

        val tempSetList = tempSet.toMutableList()
        for (tsl in tempSetList) {
            tempOcrRegisterList.add(
                OcrRegisterDTO(
                    tsl.pillEatCount,
                    tsl.pillEatDay,
                    tsl.pillEatMethod
                )
            )
        }

        for (p in pillList) {
            for (tsl in tempOcrRegisterList) {
                if (tsl.pillEatCount == p.pillEatCount && tsl.pillEatDay == p.pillEatDay && tsl.pillEatMethod == p.pillEatMethod) {
                    tsl.pillNameCategoryList.add(PillNameAndCategory(p.pillName, "기타"))
                    break
                }
            }
        }

        pillSetList.value = tempOcrRegisterList

        if (tempOcrRegisterList.isNotEmpty()) {
            curIndex = 0
        } else {
            //
        }
    }

    fun changeIndex(position: Int) {
        Log.d("kmj", "변환되기 전")
        Log.d("kmj", "변환됨.")
        isSelect1.value = pillSetList.value?.elementAt(position)?.isSelect1Value
        isSelect2.value = pillSetList.value?.elementAt(position)?.isSelect2Value
        isSelect3.value = pillSetList.value?.elementAt(position)?.isSelect3Value
        Log.d("kmj", "변환됨.${isSelect1.value},${isSelect2.value},${isSelect3.value}")
        startTimeInt = pillSetList.value!!.elementAt(position).startTimeInt
        cycleTimeInt = pillSetList.value!!.elementAt(position).cycleTimeInt

        morningEatTime = pillSetList.value!!.elementAt(position).morningEatTimeString
        lunchEatTime = pillSetList.value!!.elementAt(position).lunchEatTimeString
        dinnerEatTime = pillSetList.value!!.elementAt(position).dinnerEatTimeString

        specificTimeItemLiveData.value =
            pillSetList.value?.elementAt(position)?.specificTimeItemList

        pillNameCategoryListLiveData.value =
            pillSetList.value!!.elementAt(position).pillNameCategoryList

        val exYear = pillSetList.value!!.elementAt(position).expirationYearInt
        val exMonth = pillSetList.value!!.elementAt(position).expirationMonthInt
        val exDay = pillSetList.value!!.elementAt(position).expirationDayInt
        expirationYearInt = exYear
        expirationMonthInt = exMonth
        expirationDayInt = exDay

//        } else if (position + 1 == pillSetList.value!!.size) {
//            // 추가....
//        }
    }

    fun onClickFinish() {
        finishEvent.value = true
    }

    data class OcrPillDTO(
        val pillName: String,
        val pillEatCount: String,
        val pillEatDay: String,
        val pillEatMethod: String
    )

    data class OcrSetDTO(
        val pillEatCount: String,
        val pillEatDay: String,
        val pillEatMethod: String
    )

    data class OcrRegisterDTO(
        val pillEatCount: String,
        val pillEatDay: String,
        val pillEatMethod: String,
        // 약 이름과 종류.
        var pillNameCategoryList: MutableList<PillNameAndCategory>
        = mutableListOf<PillNameAndCategory>(),
        // 시간 선택한 값.
        var isSelect1Value: Boolean = true,
        var isSelect2Value: Boolean = false,
        var isSelect3Value: Boolean = false,
        // 시작 시간과 주기
        var startTimeInt: Int = 12,
        var cycleTimeInt: Int = 0,
        // 아침,점심,저녁 기준 먹는 시간
        var morningEatTimeString: String = "복용안함",
        var lunchEatTimeString: String = "복용안함",
        var dinnerEatTimeString: String = "복용안함",
        // 특정시간들만 골라서 먹는 시간.
        var specificTimeItemList: MutableList<SpecificTime> = mutableListOf<SpecificTime>(),
        //마감기한
        var expirationYearInt: Int = 0,
        var expirationMonthInt: Int = 0,
        var expirationDayInt: Int = 0
    )
}