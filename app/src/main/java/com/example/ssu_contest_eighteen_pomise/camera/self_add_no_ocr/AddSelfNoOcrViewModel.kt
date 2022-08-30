package com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.network.LoginService
import com.example.ssu_contest_eighteen_pomise.room_db_and_dto.RegisteredPill
import com.example.ssu_contest_eighteen_pomise.sharedpreferences.SettingSharedPreferences
import com.yourssu.design.system.atom.Checkbox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class AddSelfNoOcrViewModel(application: Application) : AndroidViewModel(application) {
    private val shPre = App.token_prefs
    private val settShrpe = SettingSharedPreferences.setInstance(application)

    val finishEvent = MutableLiveData<Boolean>()
    val addPrescriptionEvent = MutableLiveData<Boolean>()
    val failedAddPrescriptionEvent = MutableLiveData<Boolean>()
    val size: MutableLiveData<Int> = MutableLiveData(Checkbox.SMALL)
    val isSelect1: MutableLiveData<Boolean> = MutableLiveData(true)
    val isSelect2: MutableLiveData<Boolean> = MutableLiveData(false)
    val isSelect3: MutableLiveData<Boolean> = MutableLiveData(false)
    val addErrorString = MutableLiveData<PillAddErrorType>()

    var expirationYearInt: Int = 0
        set(value) {
            field = value
            expirationYear.value = "$field"
        }
    var expirationMonthInt: Int = 0
        set(value) {
            field = value
            expirationMonth.value = "$field"
        }
    var expirationDayInt: Int = 0
        set(value) {
            field = value
            expirationDay.value = "$field"
        }
    val expirationYear = MutableLiveData<String>("")
    val expirationMonth = MutableLiveData<String>("")
    val expirationDay = MutableLiveData<String>("")

    val specificTimeItemLiveData = MutableLiveData<MutableList<SpecificTime>>()
    var specificTimeHourInt: Int = 0
        set(value) {
            field = value
            specificTimeHourString.value = "${field}"
        }
    val specificTimeHourString = MutableLiveData<String>()
    var specificTimeMinutesInt: Int = 0
        set(value) {
            field = value
            specificTimeMinutesString.value = "${field}"
        }
    val specificTimeMinutesString = MutableLiveData<String>()

    var startTimeInt: Int = 12
        set(value) {
            field = value
            startTimeString.value = ("$field")
            cycleTimeList.value = AddSelfNoOcrActivity.timeList.slice(0..23 - startTimeInt)
        }
    val startTimeString = MutableLiveData<String>()

    var cycleTimeList = MutableLiveData(AddSelfNoOcrActivity.timeList.slice(0..11))
    var cycleTimeInt: Int = 0
        set(value) {
            field = value
            cycleTimeString.value = ("$field")
        }
    val cycleTimeString = MutableLiveData<String>()

    val morningEatTimeString = MutableLiveData<String>("복용 안함")
    val lunchEatTimeString = MutableLiveData<String>("복용 안함")
    val dinnerEatTimeString = MutableLiveData<String>("복용 안함")

    var changeCategoryPosition = 0
    val pillNameCategoryListLiveData = MutableLiveData<MutableList<PillNameAndCategory>>()
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

    val isPillAddButtonisDisabled = MutableLiveData(true)
    fun isCanPillAdd() {
        isPillAddButtonisDisabled.value =
            pillNameString.isBlank() || pillCategoryInt == 0
    }

    init {
        startTimeInt = 12
        cycleTimeInt = 0
        specificTimeHourInt = 12
        specificTimeMinutesInt = 30
        specificTimeItemLiveData.value = mutableListOf<SpecificTime>()
        morningEatTimeString.value = AddSelfNoOcrActivity.eatPillTimeList[0]
        lunchEatTimeString.value = AddSelfNoOcrActivity.eatPillTimeList[0]
        dinnerEatTimeString.value = AddSelfNoOcrActivity.eatPillTimeList[0]

        pillNameCategoryListLiveData.value = mutableListOf<PillNameAndCategory>()
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
        }
    }

    fun specificTimeItemOnClick(position: Int) {
        if (isSelect3.value!!) {
            specificTimeItemLiveData.value?.removeAt(position)
            specificTimeItemLiveData.value = specificTimeItemLiveData.value
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
            pillNameCategoryListLiveData.value = newSet.toMutableList()
        }
    }

    fun pillNameCategoryOnDeleteClick(position: Int) {
        pillNameCategoryListLiveData.value?.removeAt(position)
        pillNameCategoryListLiveData.value = pillNameCategoryListLiveData.value
    }

    fun onClickAdd() {
        val registeredPill = mutableListOf<RegisteredPill>()
        val timeList = mutableListOf<SpecificTime>()
        //여기서는 임의로 아침식사는 7시30분, 점심식사는 12시, 저녁식사는 5시 30분으로 하겠음.
        val morningIndex = AddSelfNoOcrActivity.eatPillTimeList.indexOf(morningEatTimeString.value)
        val lunchIndex = AddSelfNoOcrActivity.eatPillTimeList.indexOf(lunchEatTimeString.value)
        val dinnerIndex = AddSelfNoOcrActivity.eatPillTimeList.indexOf(dinnerEatTimeString.value)
        Log.d("kmj", "인덱스: ${morningIndex},${lunchIndex},${dinnerIndex}")
        //검증... 입력 다했나
        if (isSelect1.value!! and (cycleTimeInt != 0)) {
            var start_time = startTimeInt
            while (start_time < 24) {
                timeList.add(SpecificTime(start_time, 0, 0))
                start_time += cycleTimeInt
            }
        } else if (
            isSelect2.value!! and
            (morningIndex != 0 || lunchIndex != 0 || dinnerIndex != 0)
        ) {
            if (morningIndex > 0) {
                val morningSpecificTime = adjustSpecificTime(EatTime.MORNING, morningIndex)
                timeList.add(morningSpecificTime)
            }
            if (lunchIndex > 0) {
                val lunchSpecificTime = adjustSpecificTime(EatTime.LUNCH, lunchIndex)
                timeList.add(lunchSpecificTime)
            }
            if (dinnerIndex > 0) {
                val dinnerSpecificTime = adjustSpecificTime(EatTime.DINNER, dinnerIndex)
                timeList.add(dinnerSpecificTime)
            }
        } else if (isSelect3.value!! && specificTimeItemLiveData.value?.isNotEmpty() == true) {
            timeList.addAll(specificTimeItemLiveData.value!!)
        } else {
            addErrorString.value = PillAddErrorType.ONE_CHOOSE_EAT_TIME
            return
        }

        if (timeList.isEmpty()) {
            addErrorString.value = PillAddErrorType.ONE_CHOOSE_EAT_TIME
            return
        }

        if (pillNameCategoryListLiveData.value?.isEmpty() == true) {
            addErrorString.value = PillAddErrorType.ONE_REGISTER_PILL_CATEGORY
            return
        }

        //달력에서 사용자가 입력한 약 복용 마감날짜.
        if (expirationMonthInt == 0) {
            addErrorString.value = PillAddErrorType.REGISTER_PILL_DATE
            return
        }

        for (pill in pillNameCategoryListLiveData.value!!) {
            registeredPill.add(
                RegisteredPill(
                    pill.pillName,
                    pill.pillCategory,
                    timeList,
                    expirationYearInt,
                    expirationMonthInt,
                    expirationDayInt
                )
            )
        }

        insert(registeredPill)
    }


    //아침<점심<저녁 시간이 보장된다는 기준임.
    fun adjustSpecificTime(eatTime: EatTime, index: Int): SpecificTime {
        val adjSpecTime = SpecificTime(0, 0, 0)

        when (eatTime) {
            EatTime.MORNING -> {
                var morningMinutes = settShrpe.morningMin!! + (index - 2) * 30
                Log.d("kmj", "아침분: ${morningMinutes},인덱스: ${index}")
                var morningHour = settShrpe.morningHour!!

                if (morningMinutes < 0) {
                    morningMinutes += 60
                    morningHour -= 1
                } else if (morningMinutes >= 60) {
                    morningMinutes -= 60
                    morningHour += 1
                }

                Log.d("kmj", "아침 시간: ${morningHour}")
                adjSpecTime.hour = morningHour
                adjSpecTime.minutes = morningMinutes
            }
            EatTime.LUNCH -> {
                var lunchMinutes = settShrpe.lunchMin!! + (index - 2) * 30
                Log.d("kmj", "점심분: ${lunchMinutes},인덱스: ${index}")
                var lunchHour = settShrpe.lunchHour!!

                if (lunchMinutes < 0) {
                    lunchMinutes += 60
                    lunchHour -= 1
                } else if (lunchMinutes >= 60) {
                    lunchMinutes -= 60
                    lunchHour += 1
                }

                Log.d("kmj", "점심 시간: ${lunchHour}")
                adjSpecTime.hour = lunchHour
                adjSpecTime.minutes = lunchMinutes
            }
            EatTime.DINNER -> {
                var dinnerMinutes = settShrpe.dinnerMin!! + (index - 2) * 30
                var dinnerHour = settShrpe.dinnerHour!!

                if (dinnerMinutes < 0) {
                    dinnerMinutes += 60
                    dinnerHour -= 1
                } else if (dinnerMinutes >= 60) {
                    dinnerMinutes -= 60
                    dinnerHour += 1
                }
                adjSpecTime.hour = dinnerHour
                adjSpecTime.minutes = dinnerMinutes
            }
        }
        Log.d("kmj", "조정된 시간: ${adjSpecTime.hour},${adjSpecTime.minutes}")
        return adjSpecTime
    }

    fun insert(pillAddContentList: List<RegisteredPill>) {
        //뷰모델에서 코루틴 사용할때는 viewModelScope를 사용함.
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                App.loginService.registerPillRequest(shPre.refreshToken!!, pillAddContentList)
            if (response.isSuccessful) {
                runBlocking(Dispatchers.Main) {
                    addPrescriptionEvent.value = true
                }
            } else {
                addErrorString.postValue(PillAddErrorType.REGISTER_ERROR)
            }
        }
    }

    fun curCategoryChange(changeIndex: Int, changeValue: String) {
        pillNameCategoryListLiveData.value!![changeCategoryPosition].pillCategory =
            changeValue
        pillNameCategoryListLiveData.value = pillNameCategoryListLiveData.value!!
    }

    fun onClickFinish() {
        finishEvent.value = true
    }
}