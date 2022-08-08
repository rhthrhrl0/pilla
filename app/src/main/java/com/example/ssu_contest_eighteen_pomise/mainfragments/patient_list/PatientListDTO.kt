package com.example.ssu_contest_eighteen_pomise.mainfragments.patient_list

import androidx.lifecycle.MutableLiveData
import com.example.ssu_contest_eighteen_pomise.R

data class PatientListDTO(
    val name:String,
    val email:String
){
    val isSelectedBackground=MutableLiveData(R.drawable.patient_selected_background)
    val isNonSelectedBackground=MutableLiveData(R.drawable.patient_non_selected_background)

    val isSelectedLiveData=MutableLiveData<Boolean>(false)
    var isSelected=false
        set(value) {
            field=value
            isSelectedLiveData.postValue(value)
        }
}
