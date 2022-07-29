package com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.room_db_and_dto.PillDataBase
import com.yourssu.design.system.atom.Checkbox

class AddSelfNoOcrViewModel(application: Application) : AndroidViewModel(application) {
    private val shPre= App.token_prefs
    private val db = Room.databaseBuilder(
        application,
        PillDataBase::class.java, "pill-database-${shPre.email}"
    ).build()


    val finishEvent = MutableLiveData<Boolean>()
    val addPrescriptionEvent = MutableLiveData<Boolean>()
    val failedAddPrescriptionEvent = MutableLiveData<Boolean>()
    val size: MutableLiveData<Int> = MutableLiveData(Checkbox.SMALL)
    val isSelect1: MutableLiveData<Boolean> = MutableLiveData(true)
    val isSelect2: MutableLiveData<Boolean> = MutableLiveData(false)
    val isSelect3: MutableLiveData<Boolean> = MutableLiveData(false)
    var expirationYearInt:Int=0
        set(value){
            field=value
            expirationYear.value="$field"
        }
    var expirationMonthInt:Int=0
        set(value){
            field=value
            expirationMonth.value="$field"
        }
    var expirationDayInt:Int=0
        set(value){
            field=value
            expirationDay.value="$field"
        }
    val expirationYear=MutableLiveData<String>("")
    val expirationMonth=MutableLiveData<String>("")
    val expirationDay=MutableLiveData<String>("")

    val specificTimeItemLiveData = MutableLiveData<MutableList<SpecificTime>>()
    var specificTimeHourInt:Int = 0
        set(value) {
            field=value
            specificTimeHourString.value="${field}"
        }
    val specificTimeHourString=MutableLiveData<String>()
    var specificTimeMinutesInt:Int = 0
        set(value) {
            field=value
            specificTimeMinutesString.value="${field}"
        }
    val specificTimeMinutesString=MutableLiveData<String>()


    var startTimeInt: Int = 0
        set(value) {
            field = value
            startTimeString.value = ("" + field)
        }
    val startTimeString = MutableLiveData<String>()

    var cycleTimeInt: Int = 0
        set(value) {
            field = value
            cycleTimeString.value = ("" + field)
        }
    val cycleTimeString = MutableLiveData<String>()

    val morningEatTimeString=MutableLiveData<String>("")
    val lunchEatTimeString=MutableLiveData<String>("")
    val dinnerEatTimeString=MutableLiveData<String>("")

    val pillNameCategoryListLiveData = MutableLiveData<MutableList<PillNameAndCategory>>()
    private var pillNameString:String=""
    fun onPillNameTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        pillNameString = s.toString()
        isCanPillAdd()
    }
    var pillCategoryInt:Int=0
        set(value) {
            field=value
            pillCategoryString.value=AddSelfNoOcrActivity.pillCategoryList[value]
            isCanPillAdd()
        }
    var pillCategoryString=MutableLiveData<String>("선택안함")

    val isPillAddButtonisDisabled = MutableLiveData(true)
    fun isCanPillAdd(){
        if (pillNameString.isBlank() || pillCategoryString.value=="선택안함") {
            isPillAddButtonisDisabled.value=true
        } else {
            isPillAddButtonisDisabled.value=false
        }
    }

    init {
        startTimeInt = 12
        cycleTimeInt = 1
        specificTimeHourInt=12
        specificTimeMinutesInt=30
        specificTimeItemLiveData.value=  mutableListOf<SpecificTime>()
        morningEatTimeString.value=AddSelfNoOcrActivity.eatPillTimeList[0]
        lunchEatTimeString.value=AddSelfNoOcrActivity.eatPillTimeList[0]
        dinnerEatTimeString.value=AddSelfNoOcrActivity.eatPillTimeList[0]

        pillNameCategoryListLiveData.value= mutableListOf<PillNameAndCategory>()
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

    fun addSpecificTimeItem(){
        specificTimeItemLiveData.value?.add(SpecificTime(specificTimeHourInt,specificTimeMinutesInt,0))
        val newArray=specificTimeItemLiveData.value?.sortedWith(compareBy({ it.hour }, { it.minutes }))
        val newSet= newArray?.toMutableSet()
        // 관찰 당하고 있으므로 어댑터 쪽 업데이트 할거임.
        if (newSet!=null) {
            specificTimeItemLiveData.value = newSet.toMutableList()
        }
    }

    fun specificTimeItemOnClick(position:Int){
        if (isSelect3.value!!) {
            specificTimeItemLiveData.value?.removeAt(position)
            specificTimeItemLiveData.value = specificTimeItemLiveData.value
        }
    }

    fun addPillNameCategoryItem(){
        pillNameCategoryListLiveData.value?.add(PillNameAndCategory(pillNameString,pillCategoryString.value!!))
        val newSet=pillNameCategoryListLiveData.value?.toMutableSet()
        if (newSet!=null){
            pillNameCategoryListLiveData.value=newSet.toMutableList()
        }
    }

    fun pillNameCategoryOnDeleteClick(position:Int){
        pillNameCategoryListLiveData.value?.removeAt(position)
        pillNameCategoryListLiveData.value=pillNameCategoryListLiveData.value
    }

    fun onClickAdd() {
        //검증... 입력 다했나
        //다했으면 실행.

        addPrescriptionEvent.value = true
    }

    fun onClickFinish() {
        finishEvent.value = true
    }
}