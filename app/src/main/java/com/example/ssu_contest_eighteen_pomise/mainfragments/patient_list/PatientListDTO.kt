package com.example.ssu_contest_eighteen_pomise.mainfragments.patient_list

import androidx.lifecycle.MutableLiveData
import com.example.ssu_contest_eighteen_pomise.R

data class PatientListDTO(
    val name:String,
    val email:String,
    var isSelecteds:Boolean=false
){
    val isSelectedLiveData=MutableLiveData<Boolean>(false)
    var isSelected=false
        set(value) {
            field=value
            isSelectedLiveData.postValue(value)
            isSelecteds=value
        }
    init{
        isSelected=isSelecteds
    }

    fun copy()=PatientListDTO(name,email,isSelecteds)
}
