package com.example.ssu_contest_eighteen_pomise.camera.add_by_ocr

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.*
import com.example.ssu_contest_eighteen_pomise.dto.OcrAndImageData
import com.example.ssu_contest_eighteen_pomise.room_db_and_dto.RegisteredPill
import com.example.ssu_contest_eighteen_pomise.sharedpreferences.SettingSharedPreferences
import com.yourssu.design.system.atom.Checkbox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AddRegisterOcrViewModel(application: Application) : AndroidViewModel(application) {
    private val shPre = App.token_prefs
    private val settShrpe = SettingSharedPreferences.setInstance(application)
    private var addNum = 0

    lateinit var ocrImageDataInit: OcrAndImageData
    var pillSetList = MutableLiveData<MutableList<OcrRegisterDTO>>()
    var curIndex = -1

    val size: MutableLiveData<Int> = MutableLiveData(Checkbox.SMALL)

    private val _eventFlow = MutableSharedFlow<MyEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val addPrescriptionEvent = MutableLiveData<Boolean>()

    val isSelect1: MutableLiveData<Boolean> = MutableLiveData(true)
    val isSelect2: MutableLiveData<Boolean> = MutableLiveData(false)
    val isSelect3: MutableLiveData<Boolean> = MutableLiveData(false)

    var startTimeInt: Int = 12
        set(value) {
            field = value
            startTimeString.value = ("$field")
            cycleTimeList.value=AddSelfNoOcrActivity.timeList.slice(0..23-startTimeInt)
            if (curIndex >= 0) {
                pillSetList.value?.elementAt(curIndex)?.startTimeInt = value
            }
        }
    val startTimeString = MutableLiveData<String>("12")

    var cycleTimeList=MutableLiveData(AddSelfNoOcrActivity.timeList.slice(0..11))
    var cycleTimeInt: Int = 0
        set(value) {
            field = value
            cycleTimeString.value = ("$field")
            if (curIndex >= 0) {
                pillSetList.value?.elementAt(curIndex)?.cycleTimeInt = value
            }
        }
    val cycleTimeString = MutableLiveData<String>("0")

    var morningEatTime: String = "복용 안함"
        set(value) {
            field = value
            morningEatTimeString.value = value
            if (curIndex >= 0) {
                pillSetList.value?.elementAt(curIndex)?.morningEatTimeString = value
            }
        }
    var lunchEatTime: String = "복용 안함"
        set(value) {
            field = value
            lunchEatTimeString.value = value
            if (curIndex >= 0) {
                pillSetList.value?.elementAt(curIndex)?.lunchEatTimeString = value
            }
        }
    var dinnerEatTime: String = "복용 안함"
        set(value) {
            field = value
            dinnerEatTimeString.value = value
            if (curIndex >= 0) {
                pillSetList.value?.elementAt(curIndex)?.dinnerEatTimeString = value
            }
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

    var changeCategoryPosition: Int = 0
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
                if (curIndex >= 0) {
                    pillSetList.value?.elementAt(curIndex)?.expirationYearInt = value
                }
            } else {
                field = value
                expirationYear.value = ""
                if (curIndex >= 0) {
                    pillSetList.value?.elementAt(curIndex)?.expirationYearInt = 0
                }
            }
        }
    var expirationMonthInt: Int = 0
        set(value) {
            if (value > 0) {
                field = value
                expirationMonth.value = "$field"
                if (curIndex >= 0) {
                    pillSetList.value?.elementAt(curIndex)?.expirationMonthInt = value
                }
            } else {
                field = 0
                expirationMonth.value = ""
                if (curIndex >= 0) {
                    pillSetList.value?.elementAt(curIndex)?.expirationMonthInt = 0
                }
            }
        }
    var expirationDayInt: Int = 0
        set(value) {
            if (value > 0) {
                field = value
                expirationDay.value = "$field"
                if (curIndex >= 0) {
                    pillSetList.value?.elementAt(curIndex)?.expirationDayInt = value
                }
            } else {
                field = value
                expirationDay.value = ""
                if (curIndex >= 0) {
                    pillSetList.value?.elementAt(curIndex)?.expirationDayInt = 0
                }
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

        if (curIndex >= 0) {
            pillSetList.value?.elementAt(curIndex)?.isSelect1Value = isSelect1.value!!
            pillSetList.value?.elementAt(curIndex)?.isSelect2Value = isSelect2.value!!
            pillSetList.value?.elementAt(curIndex)?.isSelect3Value = isSelect3.value!!
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
        if (newSet != null && (curIndex >= 0)) {
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

    //초기에 한번만 수행.
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

        tempOcrRegisterList.map {
            it.typeId = NON_SELECTED_SET_ITEM
        }
        if (tempOcrRegisterList.size > 0) {
            tempOcrRegisterList[0].typeId = SELECTED_SET_ITEM
        }
        val plusAddObject = OcrRegisterDTO("플러스용", "플러스용", "플러스용").apply {
            typeId = OCR_PLUS_ITEM
        }
        tempOcrRegisterList.add(plusAddObject)

        pillSetList.value = tempOcrRegisterList

        if (tempOcrRegisterList.size == 1) {
            sendEvent(MyEvent.EmptySetEvent)
            curIndex = -1
        } else {
            sendEvent(MyEvent.NotEmptySetEvent)
            changeIndex(0)
        }
    }

    fun curCategoryChange(changeIndex: Int, changeValue: String) {
        pillSetList.value!![curIndex].pillNameCategoryList[changeCategoryPosition].pillCategory =
            changeValue
        pillNameCategoryListLiveData.value = pillSetList.value!![curIndex].pillNameCategoryList
    }


    fun onDeleteItemSelectedClick(position: Int) {
        // 선택된 아이템이 삭제요청을 받은 경우
        deleteSelectedItem(position)
    }

    fun onNonSelectedItemClick(position: Int) {
        // 아직 선택받지 못한 아이템이 선택요청을 받는 경우
        changeIndex(position)
    }

    fun onDeleteItemNonSelectedClick(position: Int) {
        // 아직 선택받지 못한 아이템이 삭제요청을 받는 경우
        deleteNonSelectedItem(position)
    }

    fun onPlusItemClick(position: Int) {
        // 그 자리로 추가 시켜야 함.
        changeIndex(position, true)
    }

    // 선택될 놈을 선택해주셈.                // 그자리에 새로 추가되어야 하는 것인지 판별.
    fun changeIndex(position: Int, addPermission: Boolean = false) {
        val newPillSetList = copyPillSetList()
        if (addPermission) {
            newPillSetList.add(
                position,
                OcrRegisterDTO("", "", "기타${addNum++}", SELECTED_SET_ITEM)
            )
        }
        newPillSetList.map {
            it.typeId = NON_SELECTED_SET_ITEM
        }
        newPillSetList[position].typeId = SELECTED_SET_ITEM
        newPillSetList.lastOrNull()?.typeId = OCR_PLUS_ITEM
        curIndex = position

        changeContents(position, newPillSetList)
        pillSetList.value = newPillSetList
    }

    fun changeContents(position: Int, newPillSetList: MutableList<OcrRegisterDTO>) {
        isSelect1.value = newPillSetList.elementAt(position).isSelect1Value
        isSelect2.value = newPillSetList.elementAt(position).isSelect2Value
        isSelect3.value = newPillSetList.elementAt(position).isSelect3Value

        startTimeInt = newPillSetList.elementAt(position).startTimeInt
        cycleTimeInt = newPillSetList.elementAt(position).cycleTimeInt

        morningEatTime = newPillSetList.elementAt(position).morningEatTimeString
        lunchEatTime = newPillSetList.elementAt(position).lunchEatTimeString
        dinnerEatTime = newPillSetList.elementAt(position).dinnerEatTimeString

        specificTimeItemLiveData.value =
            newPillSetList.elementAt(position).specificTimeItemList

        pillNameCategoryListLiveData.value =
            newPillSetList.elementAt(position).pillNameCategoryList

        val exYear = newPillSetList.elementAt(position).expirationYearInt
        val exMonth = newPillSetList.elementAt(position).expirationMonthInt
        val exDay = newPillSetList.elementAt(position).expirationDayInt
        expirationYearInt = exYear
        expirationMonthInt = exMonth
        expirationDayInt = exDay
    }

    fun deleteSelectedItem(position: Int) {
        val newPillSetList = copyPillSetList()
        newPillSetList.removeAt(position)
        newPillSetList.map {
            it.typeId = NON_SELECTED_SET_ITEM
        }
        if (newPillSetList.size > 1) {
            newPillSetList[0].typeId = SELECTED_SET_ITEM
            newPillSetList.lastOrNull()?.typeId = OCR_PLUS_ITEM
            curIndex = 0
            pillSetList.value = newPillSetList
            changeContents(0, newPillSetList)
        } else {
            newPillSetList[0].typeId = OCR_PLUS_ITEM
            curIndex = -1
            pillSetList.value = newPillSetList
            clear()
        }
    }

    fun deleteNonSelectedItem(position: Int) {
        val newPillSetList = copyPillSetList()
        newPillSetList.removeAt(position)
        if (newPillSetList.size > 1) {
            newPillSetList.lastOrNull()?.typeId = OCR_PLUS_ITEM
            for (ps in newPillSetList) {
                if (ps.typeId == SELECTED_SET_ITEM) {
                    curIndex = newPillSetList.indexOf(ps)
                }
            }
            pillSetList.value = newPillSetList
            changeContents(curIndex, newPillSetList)
        } else {
            newPillSetList[0].typeId = OCR_PLUS_ITEM
            curIndex = -1
            pillSetList.value = newPillSetList
            clear()
        }
    }

    fun copyPillSetList(): MutableList<OcrRegisterDTO> {
        val newPillSetList = mutableListOf<OcrRegisterDTO>()
        for (p in pillSetList.value ?: emptyList()) {
            newPillSetList.add(p.copy())
        }
        return newPillSetList
    }

    fun clear() {
        isSelect1.value = true
        isSelect2.value = false
        isSelect3.value = false

        startTimeInt = 12
        cycleTimeInt = 0

        morningEatTime = "복용안함"
        lunchEatTime = "복용안함"
        dinnerEatTime = "복용안함"

        specificTimeItemLiveData.value = mutableListOf()

        pillNameCategoryListLiveData.value = mutableListOf()

        val exYear = 0
        val exMonth = 0
        val exDay = 0
        expirationYearInt = exYear
        expirationMonthInt = exMonth
        expirationDayInt = exDay
    }

    var addNow = false
    fun onClickAdd() {
        if (!addNow) {
            addNow = true
            val nowList = copyPillSetList()
            if (nowList.size == 1) {
                sendEvent(MyEvent.AddFailedNoListEvent)
            }

            val registeredPill = mutableListOf<RegisteredPill>()
            for (i in 0 until nowList.size - 1) {
                val nowPageOB = nowList[i]
                val timeList = mutableListOf<SpecificTime>()
                //여기서는 임의로 아침식사는 7시30분, 점심식사는 12시, 저녁식사는 5시 30분으로 하겠음.
                val morningIndex =
                    AddSelfNoOcrActivity.eatPillTimeList.indexOf(nowPageOB.morningEatTimeString)
                val lunchIndex =
                    AddSelfNoOcrActivity.eatPillTimeList.indexOf(nowPageOB.lunchEatTimeString)
                val dinnerIndex =
                    AddSelfNoOcrActivity.eatPillTimeList.indexOf(nowPageOB.dinnerEatTimeString)

                //검증... 입력 다했나
                if (nowPageOB.isSelect1Value and (nowPageOB.cycleTimeInt != 0)) {
                    var start_time = nowPageOB.startTimeInt
                    while (start_time < 24) {
                        timeList.add(SpecificTime(start_time, 0, 0))
                        start_time += nowPageOB.cycleTimeInt
                    }
                } else if (
                    nowPageOB.isSelect2Value and
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
                } else if (nowPageOB.isSelect3Value && nowPageOB.specificTimeItemList.isNotEmpty()) {
                    timeList.addAll(specificTimeItemLiveData.value!!)
                } else {
                    sendEvent(
                        MyEvent.AddFailedEvent(
                            i,
                            nowPageOB.pillEatMethod,
                            "복용시간을 한개 이상 선택해주세요."
                        )
                    )
                    return
                }

                if (timeList.isEmpty()){
                    sendEvent(
                        MyEvent.AddFailedEvent(
                            i,
                            nowPageOB.pillEatMethod,
                            "복용시간을 정확하게 입력해주세요."
                        )
                    )
                    return
                }

                if (nowPageOB.pillNameCategoryList.isEmpty()) {
                    sendEvent(
                        MyEvent.AddFailedEvent(
                            i,
                            nowPageOB.pillEatMethod,
                            "복용할 약을 한개 이상 선택해주세요."
                        )
                    )
                    return
                }

                //달력에서 사용자가 입력한 약 복용 마감날짜.
                if (nowPageOB.expirationMonthInt == 0) {
                    sendEvent(
                        MyEvent.AddFailedEvent(
                            i,
                            nowPageOB.pillEatMethod,
                            "복용마감 날짜를 입력해주세요."
                        )
                    )
                    return
                }

                for (pill in nowPageOB.pillNameCategoryList) {
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
            }

            insert(registeredPill)
        }
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
        val job = viewModelScope.launch(Dispatchers.IO) {
            val response =
                App.loginService.registerPillRequest(shPre.refreshToken!!, pillAddContentList)
            if (response.isSuccessful) {
                sendEvent(MyEvent.AddSuccessEvent)
            } else {
                sendEvent(MyEvent.AddFailedFromServer)
            }
        }
    }

    fun onClickFinish() {
        sendEvent(MyEvent.FinishEvent)
    }

    fun sendEvent(event: MyEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
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
        var typeId: Int = INVALID_ITEM,
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
        var morningEatTimeString: String = "복용 안함",
        var lunchEatTimeString: String = "복용 안함",
        var dinnerEatTimeString: String = "복용 안함",
        // 특정시간들만 골라서 먹는 시간.
        var specificTimeItemList: MutableList<SpecificTime> = mutableListOf<SpecificTime>(),
        //마감기한
        var expirationYearInt: Int = 0,
        var expirationMonthInt: Int = 0,
        var expirationDayInt: Int = 0
    ) {
        fun copy(): OcrRegisterDTO {
            val copyPillNameAndCategoryList = mutableListOf<PillNameAndCategory>()
            for (pnac in this.pillNameCategoryList) {
                copyPillNameAndCategoryList.add(
                    PillNameAndCategory(
                        pnac.pillName,
                        pnac.pillCategory
                    )
                )
            }

            val copySpecificTimeItemList = mutableListOf<SpecificTime>()
            for (pnac in this.specificTimeItemList) {
                copySpecificTimeItemList.add(SpecificTime(pnac.hour, pnac.minutes))
            }

            val copyOB = OcrRegisterDTO(
                this.pillEatCount,
                this.pillEatDay,
                this.pillEatMethod,
                this.typeId,
                copyPillNameAndCategoryList,
                this.isSelect1Value,
                this.isSelect2Value,
                this.isSelect3Value,
                this.startTimeInt,
                this.cycleTimeInt,
                this.morningEatTimeString,
                this.lunchEatTimeString,
                this.dinnerEatTimeString,
                copySpecificTimeItemList,
                this.expirationYearInt,
                this.expirationMonthInt,
                this.expirationDayInt
            )

            return copyOB
        }
    }

    sealed class MyEvent {
        object EmptySetEvent : MyEvent()
        object NotEmptySetEvent : MyEvent()
        object FinishEvent : MyEvent()
        class AddFailedEvent(
            val position: Int,
            val pillEatMethod: String,
            val failedReason: String
        ) : MyEvent()

        object AddSuccessEvent : MyEvent()
        object AddFailedNoListEvent : MyEvent()
        object AddFailedFromServer : MyEvent()
    }

    companion object {
        const val SELECTED_SET_ITEM = R.layout.ocr_selected_set_list_item
        const val NON_SELECTED_SET_ITEM = R.layout.ocr_non_selected_set_list_item
        const val OCR_PLUS_ITEM = R.layout.ocr_plus_object_item
        const val INVALID_ITEM = -1
    }
}