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


    fun onClickAdd() {
        //검증... 입력 다했나
        //다했으면 실행.
        addPrescriptionEvent.value = true
    }

    fun onClickFinish() {
        finishEvent.value = true
    }
}