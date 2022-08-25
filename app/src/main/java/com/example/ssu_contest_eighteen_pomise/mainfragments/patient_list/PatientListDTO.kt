package com.example.ssu_contest_eighteen_pomise.mainfragments.patient_list

data class PatientListDTO(
    val name:String,
    val email:String,
    var isSelected:Boolean=false
){
    fun copy()=PatientListDTO(name,email,isSelected)
}
