package com.example.ssu_contest_eighteen_pomise.mainfragments

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.PillNameAndCategory
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.SpecificTime
import com.example.ssu_contest_eighteen_pomise.mainfragments.list.AlarmListDTO
import com.example.ssu_contest_eighteen_pomise.room_db_and_dto.PillDataBase
import com.example.ssu_contest_eighteen_pomise.room_db_and_dto.RegisteredPill
import com.example.ssu_contest_eighteen_pomise.sharedpreferences.SettingSharedPreferences
import com.yourssu.design.system.atom.ToolTip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList

class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val shPre = App.token_prefs
    private val db = Room.databaseBuilder(
        application,
        PillDataBase::class.java, "pill-database-${shPre.email}"
    ).build()
    private val settShrpe = SettingSharedPreferences.setInstance(application)

    val refreshEndEvent = MutableLiveData<Boolean>()
    val refreshStartEvent = MutableLiveData<Boolean>()
    val refreshInitWithViewModel = MutableLiveData<Boolean>()
    val nameAndBirthString = MutableLiveData<String>()
    var nameString = ""
    var birthYear: Int = 0
    var birthMonth: Int = 0
    var birthDay: Int = 0
    var pillListItems = MutableLiveData<List<AlarmListDTO>>()

    fun refreshBtn() {
        getListItem(false)
        refreshEndEvent.postValue(true)
    }

    fun editBirthDayBtn() {

    }

    init {
        changedNameAndBirth()
        getListItem(true)
    }

    fun getListItem(isInit: Boolean) {
        refreshStartEvent.value = true //시작
        val deleteExpireDatePill = deleteExpireDate()
        val getListJob = viewModelScope.launch(Dispatchers.IO) {
            deleteExpireDatePill.join()
            val listRoomItems = db.pillDao().getAll()
            Log.d("kmj", "얻는함수 내부 전체: ${listRoomItems}")
            val listItems = mutableListOf<AlarmListDTO>()
            val timeSet = mutableSetOf<SpecificTime>()

            //시간 정보 수집.
            for (lri in listRoomItems) {
                for (t in lri.eatTime) {
                    timeSet.add(t)
                }
            }
            Log.d("kmj", "얻는함수의 시간 집합: ${timeSet}")

            for (t in timeSet) {
                listItems.add(AlarmListDTO(false, t.hour, t.minutes, mutableListOf()))
            }
            Log.d("kmj", "얻는함수의 리스트: ${listItems}")

            for (lri in listRoomItems) {
                val pillNandC = PillNameAndCategory(lri.pillName, lri.pillCategory)
                for (t in lri.eatTime) {
                    for (ald in listItems) {
                        if (ald.eatHour == t.hour && ald.eatMinutes == t.minutes) {
                            ald.pillList.add(pillNandC)
                        }
                    }
                }
            }

            //시간 별로 일단 줄세우기
            var sortedListItmes =
                listItems.sortedWith(compareBy({ it.eatHour }, { it.eatMinutes })).toMutableList()
            Log.d("kmj", "얻는함수의 정렬된 리스트: ${sortedListItmes}")
            //현재 시간보다 늦은 시간의 알람들은 제거하고 다시 뒤에 추가함.
            val t_date = Date(System.currentTimeMillis())
            Log.d("kmj", "현재 시각: ${t_date.hours},${t_date.minutes}")

            val backToMove= mutableListOf<AlarmListDTO>()
            var t_late_start_index = -1
            var t_late_end_index = -1
            for (lri in sortedListItmes) {
                if ((lri.eatHour < t_date.hours) || (lri.eatHour == t_date.hours && lri.eatMinutes < t_date.minutes)) {
                    backToMove.add(lri.copy())
                }
            }
            Log.d("kmj","뒤로 가야 하는 놈들: ${backToMove}")
            Log.d("kmj","개수: ${sortedListItmes.size}")
            Log.d("kmj","인덱스들: ${t_late_start_index},${t_late_end_index}")

            sortedListItmes.removeAll(backToMove)
            sortedListItmes.addAll(backToMove)
            if (sortedListItmes.size >= 1) {
                sortedListItmes.elementAt(0).isNextEatPill = true
            }
            Log.d("kmj", "얻는함수의 최종: ${sortedListItmes}")
            pillListItems.postValue(sortedListItmes)
        }
        runBlocking {
            deleteExpireDatePill.join()
            getListJob.join()
            // 현재 메인스레드가 아니므로 그냥 setValue말고 postValue해주기.
            if (isInit) {
                refreshInitWithViewModel.postValue(true)
            } else {
                refreshEndEvent.postValue(true)
            }
        }
    }

    fun deleteExpireDate() =
        viewModelScope.launch(Dispatchers.IO) {
            val listRoomItems = db.pillDao().getAll()
            Log.d("kmj", "삭제함수 내부 전체: ${listRoomItems}")
            val today = System.currentTimeMillis()
            for (lri in listRoomItems) {
                val cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, lri.dateYear)
                cal.set(Calendar.MONTH, lri.dateMonth - 1)
                cal.set(Calendar.DAY_OF_MONTH, lri.dateDay + 1)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                if (today > cal.timeInMillis) {
                    db.pillDao().delete(lri)
                    Log.d("kmj", "삭제요소: ${lri}")
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

    //생년월일 변경되면 홈프래그먼트 쪽에서 실행됨.
    fun changedNameAndBirth() {
        nameString = shPre.name ?: ""
        birthYear = settShrpe.birthYear ?: 0
        birthMonth = settShrpe.birthMonth ?: 0
        birthDay = settShrpe.birthDay ?: 0
        setChangedNameAndBirth()
    }

    fun setChangedNameAndBirth() {
        nameAndBirthString.value = "${nameString} (${birthYear}.${birthMonth}.${birthDay})"
    }

}