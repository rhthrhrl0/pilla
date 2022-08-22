package com.example.ssu_contest_eighteen_pomise.mainfragments

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.PillNameAndCategory
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.SpecificTime
import com.example.ssu_contest_eighteen_pomise.dto.Email
import com.example.ssu_contest_eighteen_pomise.dto.Pill
import com.example.ssu_contest_eighteen_pomise.dto.PillRecord
import com.example.ssu_contest_eighteen_pomise.mainfragments.list.AlarmListDTO
import com.example.ssu_contest_eighteen_pomise.mainfragments.patient_list.PatientListDTO
import com.example.ssu_contest_eighteen_pomise.network.LoginService
import com.example.ssu_contest_eighteen_pomise.network.UserService
import com.example.ssu_contest_eighteen_pomise.sharedpreferences.SettingSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.joda.time.format.DateTimeFormat
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val shPre = App.token_prefs
    private val settShrpe = SettingSharedPreferences.setInstance(application)

    val refreshEndEvent = MutableLiveData<Boolean>()
    val refreshFailedEvent=MutableLiveData<Boolean>()
    val refreshStartEvent = MutableLiveData<Boolean>()
    val refreshInitWithViewModel = MutableLiveData<Boolean>()
    val nameStringLiveData = MutableLiveData<String>("")
    val isGuardianLiveData = MutableLiveData<Boolean>()

    val patientListItems = MutableLiveData<List<PatientListDTO>>()
    val curPatientEmail = MutableLiveData<String>()
    var curPatientIndex = 0
        set(value) {
            field = value
            curPatientEmail.value = patientListItems.value?.elementAt(value)?.email ?: ""
            nameStringLiveData.value = patientListItems.value?.elementAt(value)?.name ?: ""
            Log.d("kmj", "스트링 내부:${nameStringLiveData.value}")
        }

    var pillListItems = MutableLiveData<List<AlarmListDTO>>()

    fun refreshBtn() {
        getListItem(false)
        refreshEndEvent.postValue(true)
    }

    init {
        if (shPre.isGuardian == true) {
            // 보호자라면
            isGuardianLiveData.value = true
            getPatientList()
        } else {
            isGuardianLiveData.value = false
            //setMyData()
        }
    }

    fun clickPatientIndex(position: Int, email: String) {
        if (position == curPatientIndex) {
            getListItem(false)
        } else {
            val copyList = mutableListOf<PatientListDTO>()
            for (p in patientListItems.value!!) {
                copyList.add(p.copy())
            }
            copyList.elementAt(curPatientIndex).isSelected = false
            copyList.elementAt(position).isSelected = true
            patientListItems.value = copyList
            curPatientIndex = position
            getListItem(true)
        }
    }

    fun getPatientList() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = App.loginService.getPatientListRequest(shPre.refreshToken!!)
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
                        patientListItems.value = tempPatientsArray
                        curPatientIndex = 0 //초깃값은 0번 인덱스로.
                    }
                }
            } else {
                //
            }
        }
    }

    fun setMyData() {
        val myData = PatientListDTO(shPre.name!!, shPre.email!!, true)
        val myArray = mutableListOf<PatientListDTO>(myData)
        patientListItems.value = myArray
        curPatientIndex = 0
    }

    fun getListItem(isInit: Boolean) {
        refreshStartEvent.value = true //시작
        viewModelScope.launch(Dispatchers.IO) {
            var pillRecords: List<Pill> = emptyList()
            val temp = mutableListOf<AlarmListDTO>()
            val timeSet = mutableSetOf<SpecificTime>()
            var responseIsSuccessful: Boolean = false

            if (shPre.isGuardian!!) {
                val curPatient = curPatientEmail.value ?: ""
                val curPatientIndex = curPatientIndex

                val response =
                    App.userService.postGetProtegePillRecord(shPre.refreshToken!!, Email(curPatient))

                if (response.isSuccessful && response.body()!!.pillCount > 0) {
                    Log.d(
                        "kmj",
                        "복용약 목록 갱신: ${shPre.refreshToken!!},${curPatient},${
                            response.body().toString()
                        }"
                    )
                    pillRecords=response.body()!!.pills
                    responseIsSuccessful = true
                } else {
                    responseIsSuccessful = false
                    Log.d("kmj", "목록 갱신 실패")
                }
            } else {
                val response =
                    App.userService.getMyPillRecord(shPre.refreshToken!!)

                if (response.isSuccessful) {
                    Log.d(
                        "kmj",
                        "복용약 목록 갱신: ${shPre.refreshToken!!},${
                            response.body().toString()
                        }"
                    )
                    pillRecords=response.body()!!
                    responseIsSuccessful = true
                } else {
                    responseIsSuccessful = false
                    Log.d("kmj", "목록 갱신 실패")
                }
            }

            if (responseIsSuccessful) {
                // 시간 정보 먼저 수집.
                for (p in pillRecords) {
                    timeSet.add(convert(p.time))
                }

                for (t in timeSet) {
                    temp.add(AlarmListDTO(false, t.hour, t.minutes, mutableListOf()))
                }

                for (p in pillRecords) {
                    val pillNandC = PillNameAndCategory(p.pillName, p.pillCategory)
                    val spt = convert(p.time)

                    for (t in temp) {
                        if (t.eatHour == spt.hour && t.eatMinutes == spt.minutes) {
                            t.pillList.add(pillNandC)
                            break
                        }
                    }
                }

                //시간 별로 일단 줄세우기
                var sortedListItmes =
                    temp.sortedWith(compareBy({ it.eatHour }, { it.eatMinutes })).toMutableList()
                Log.d("kmj", "얻는함수의 정렬된 리스트: ${sortedListItmes}")
                //현재 시간보다 늦은 시간의 알람들은 제거하고 다시 뒤에 추가함.
                val t_date = Date(System.currentTimeMillis())
                Log.d("kmj", "현재 시각: ${t_date.hours},${t_date.minutes}")

                val backToMove = mutableListOf<AlarmListDTO>()
                var t_late_start_index = -1
                var t_late_end_index = -1
                for (lri in sortedListItmes) {
                    if ((lri.eatHour < t_date.hours) || (lri.eatHour == t_date.hours && lri.eatMinutes < t_date.minutes)) {
                        backToMove.add(lri.copy())
                    }
                }
                Log.d("kmj", "뒤로 가야 하는 놈들: ${backToMove}")
                Log.d("kmj", "개수: ${sortedListItmes.size}")
                Log.d("kmj", "인덱스들: ${t_late_start_index},${t_late_end_index}")

                sortedListItmes.removeAll(backToMove)
                sortedListItmes.addAll(backToMove)
                if (sortedListItmes.size >= 1) {
                    sortedListItmes.elementAt(0).isNextEatPill = true
                }
                Log.d("kmj", "얻는함수의 최종: ${sortedListItmes}")
                pillListItems.postValue(sortedListItmes)
                runBlocking {
                    // 현재 메인스레드가 아니므로 그냥 setValue말고 postValue해주기.
                    if (isInit) {
                        refreshInitWithViewModel.postValue(true)
                    } else {
                        refreshEndEvent.postValue(true)
                    }
                }
            }
            else{
                pillListItems.postValue(emptyList())
                runBlocking {
                    refreshFailedEvent.postValue(true)
                }
            }
        }
    }


    fun tooltipString(position: Int): String {
        val sb = StringBuilder()
        sb.append("약 종류: ")
        for (item in pillListItems.value!![position].pillList) {
            sb.append("[${item.pillCategory}], ")
        }
        sb.deleteCharAt(sb.lastIndexOf(", "))
        return sb.toString()
    }

    fun convert(string: String): SpecificTime {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val ldt = LocalDateTime.parse(string, dtf)
            return SpecificTime(ldt.hour, ldt.minute, ldt.second)
        } else {
            //val date = org.joda.time.LocalDateTime.now()
            val dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
            val jodatime = dtf.parseDateTime(string)
            return SpecificTime(jodatime.hourOfDay, jodatime.minuteOfHour, jodatime.secondOfMinute)
        }
    }

}