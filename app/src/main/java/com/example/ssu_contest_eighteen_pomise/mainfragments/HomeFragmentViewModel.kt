package com.example.ssu_contest_eighteen_pomise.mainfragments

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.mainfragments.list.AlarmListDTO
import com.example.ssu_contest_eighteen_pomise.mainfragments.patient_list.PatientListDTO
import com.example.ssu_contest_eighteen_pomise.network.LoginService
import com.example.ssu_contest_eighteen_pomise.sharedpreferences.SettingSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val shPre = App.token_prefs
    private val retrofit = Retrofit.Builder()
        .baseUrl(LoginService.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    val service = retrofit.create(LoginService::class.java)
    private val settShrpe = SettingSharedPreferences.setInstance(application)

    val refreshEndEvent = MutableLiveData<Boolean>()
    val refreshStartEvent = MutableLiveData<Boolean>()
    val refreshInitWithViewModel = MutableLiveData<Boolean>()
    val nameStringLiveData = MutableLiveData<String>("")

    val patientListItems = MutableLiveData<List<PatientListDTO>>()
    val curPatientEmail = MutableLiveData<String>()
    private var curPatientIndex = 0
        set(value) {
            field = value
            curPatientEmail.value=patientListItems.value?.elementAt(value)?.email ?: ""
            nameStringLiveData.value=patientListItems.value?.elementAt(value)?.name ?: ""
            Log.d("kmj","스트링 내부:${nameStringLiveData.value}")
        }

    var pillListItems = MutableLiveData<List<AlarmListDTO>>()

    fun refreshBtn() {
        getListItem(false)
        refreshEndEvent.postValue(true)
    }

    init {
        // 이거 조건문 걸어서 보호자일때만 실행.
        getPatientList()
    }

    fun clickPatientIndex(position: Int, email: String) {
        if (position == curPatientIndex) {
            getListItem(false)
        } else {
            val copyList= mutableListOf<PatientListDTO>()
            for (p in patientListItems.value!!){
                copyList.add(p.copy())
            }
            copyList.elementAt(curPatientIndex).isSelected=false
            copyList.elementAt(position).isSelected=true
            patientListItems.value=copyList
            curPatientIndex = position
            getListItem(false)
        }
    }

    fun getPatientList() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = service.getPatientListRequest(shPre.refreshToken!!)
            if (response.isSuccessful) {
                val count = response.body()?.protegesCount ?: 0
                val patients = response.body()?.protegesInfos ?: emptyList()
                val tempPatientsArray = mutableListOf<PatientListDTO>()
                if (count == 0) {
                    patientListItems.postValue(emptyList())
                } else {
                    for (p in patients) {
                        tempPatientsArray.add(PatientListDTO(p.username, p.email))
                    }
                    runBlocking(Dispatchers.Main) {
                        tempPatientsArray.elementAt(0).isSelected = true
                        patientListItems.value=tempPatientsArray
                        curPatientIndex = 0 //초깃값은 0번 인덱스로.
                    }
                }
            } else {
                //
            }
        }
    }

    fun getListItem(isInit: Boolean) {
        refreshStartEvent.value = true //시작
        val getListJob = viewModelScope.launch(Dispatchers.IO) {
            val curPatient = curPatientEmail.value ?: ""
            val curPatientIndex = curPatientIndex
            val response = service.postGetProtegePillRecord(shPre.refreshToken!!, curPatient)
            if (response.isSuccessful) {

            } else {

            }
//            val response=service.
//            Log.d("kmj", "얻는함수 내부 전체: ${listRoomItems}")
//            val listItems = mutableListOf<AlarmListDTO>()
//            val timeSet = mutableSetOf<SpecificTime>()
//
//            //시간 정보 수집.
//            for (lri in listRoomItems) {
//                for (t in lri.eatTime) {
//                    timeSet.add(t)
//                }
//            }
//            Log.d("kmj", "얻는함수의 시간 집합: ${timeSet}")
//
//            for (t in timeSet) {
//                listItems.add(AlarmListDTO(false, t.hour, t.minutes, mutableListOf()))
//            }
//            Log.d("kmj", "얻는함수의 리스트: ${listItems}")
//
//            for (lri in listRoomItems) {
//                val pillNandC = PillNameAndCategory(lri.pillName, lri.pillCategory)
//                for (t in lri.eatTime) {
//                    for (ald in listItems) {
//                        if (ald.eatHour == t.hour && ald.eatMinutes == t.minutes) {
//                            ald.pillList.add(pillNandC)
//                        }
//                    }
//                }
//            }
//
//            //시간 별로 일단 줄세우기
//            var sortedListItmes =
//                listItems.sortedWith(compareBy({ it.eatHour }, { it.eatMinutes })).toMutableList()
//            Log.d("kmj", "얻는함수의 정렬된 리스트: ${sortedListItmes}")
//            //현재 시간보다 늦은 시간의 알람들은 제거하고 다시 뒤에 추가함.
//            val t_date = Date(System.currentTimeMillis())
//            Log.d("kmj", "현재 시각: ${t_date.hours},${t_date.minutes}")
//
//            val backToMove= mutableListOf<AlarmListDTO>()
//            var t_late_start_index = -1
//            var t_late_end_index = -1
//            for (lri in sortedListItmes) {
//                if ((lri.eatHour < t_date.hours) || (lri.eatHour == t_date.hours && lri.eatMinutes < t_date.minutes)) {
//                    backToMove.add(lri.copy())
//                }
//            }
//            Log.d("kmj","뒤로 가야 하는 놈들: ${backToMove}")
//            Log.d("kmj","개수: ${sortedListItmes.size}")
//            Log.d("kmj","인덱스들: ${t_late_start_index},${t_late_end_index}")
//
//            sortedListItmes.removeAll(backToMove)
//            sortedListItmes.addAll(backToMove)
//            if (sortedListItmes.size >= 1) {
//                sortedListItmes.elementAt(0).isNextEatPill = true
//            }
//            Log.d("kmj", "얻는함수의 최종: ${sortedListItmes}")
//            pillListItems.postValue(sortedListItmes)
        }
        runBlocking {
            getListJob.join()
            // 현재 메인스레드가 아니므로 그냥 setValue말고 postValue해주기.
            if (isInit) {
                refreshInitWithViewModel.postValue(true)
            } else {
                refreshEndEvent.postValue(true)
            }
        }
    }


    fun tooltipString(position: Int): String {
        val sb = StringBuilder()
        for (item in pillListItems.value!![position].pillList) {
            sb.append("${item.pillCategory}, ")
        }
        sb.deleteCharAt(sb.lastIndexOf(", "))
        return sb.toString()
    }

}