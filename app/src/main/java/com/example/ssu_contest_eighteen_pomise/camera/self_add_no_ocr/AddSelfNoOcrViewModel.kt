package com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.yourssu.design.system.atom.Checkbox

class AddSelfNoOcrViewModel(application: Application) : AndroidViewModel(application) {
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
    init {
        startTimeInt = 12
        cycleTimeInt = 1
        specificTimeHourInt=12
        specificTimeMinutesInt=30
        specificTimeItemLiveData.value=  mutableListOf<SpecificTime>()
        morningEatTimeString.value=AddSelfNoOcrActivity.eatPillTimeList[0]
        lunchEatTimeString.value=AddSelfNoOcrActivity.eatPillTimeList[0]
        dinnerEatTimeString.value=AddSelfNoOcrActivity.eatPillTimeList[0]
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

    fun onClickAdd() {
        //검증... 입력 다했나
        //다했으면 실행.
        addPrescriptionEvent.value = true
    }

    fun onClickFinish() {
        finishEvent.value = true
    }
}