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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

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
            val listItems = mutableListOf<AlarmListDTO>()
            val timeSet = mutableSetOf<SpecificTime>()

            //시간 정보 수집.
            for (lri in listRoomItems) {
                for (t in lri.eatTime) {
                    timeSet.add(t)
                }
            }

            for (t in timeSet){
                listItems.add(AlarmListDTO(false,t.hour,t.minutes, mutableListOf()))
            }

            for (lri in listRoomItems){
                val pillNandC=PillNameAndCategory(lri.pillName,lri.pillCategory)
                for ( t in lri.eatTime) {
                    for (ald in listItems) {
                        if(ald.eatHour==t.hour && ald.eatMinutes == t.minutes){
                            ald.pillList.add(pillNandC)
                        }
                    }
                }
            }


//            listItems.add(AlarmListDTO(false, 3, 20, emptyList()))
//            listItems.add(AlarmListDTO(false, 2, 30, emptyList()))
//            listItems.add(AlarmListDTO(false, 2, 40, emptyList()))

            //시간 별로 일단 줄세우기
            var sortedListItmes = listItems.sortedWith(compareBy({ it.eatHour }, { it.eatMinutes })).toMutableList()

            //현재 시간보다 늦은 시간의 알람들은 제거하고 다시 뒤에 추가함.
            val t_date = Date(System.currentTimeMillis())
            var t_late_start_index=-1
            var t_late_end_index=-1
            for (lri in sortedListItmes) {
                if ((lri.eatHour < t_date.hours) || (lri.eatHour==t_date.hours && lri.eatMinutes<t_date.minutes)) {
                    if(t_late_start_index==-1) { //아직 시작지점이 안나왔다면
                        t_late_start_index = sortedListItmes.indexOf(lri)
                    }
                }
                else{
                    if(t_late_start_index!=-1){ //뒤로 리스트 순서를 바꿔야 하는 지난 시간이 잇다면,
                        t_late_end_index=sortedListItmes.indexOf(lri)
                        break
                    }
                }
            }
            if(t_late_end_index+1==sortedListItmes.size){
                sortedListItmes= mutableListOf<AlarmListDTO>()
            }
            else if(t_late_end_index!=-1){
                val front_late_list=sortedListItmes.subList(0,t_late_end_index)
                sortedListItmes.removeAll(front_late_list)
                sortedListItmes.addAll(front_late_list)
                // 빼고 넣어서 뒤에 대기시키기.
            }

            if (sortedListItmes.size >= 1) {
                sortedListItmes.elementAt(0).isNextEatPill = true
            }
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
                }
            }
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