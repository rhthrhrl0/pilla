package com.example.ssu_contest_eighteen_pomise.mainfragments.pill_manage

import android.app.Application
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.SpecificTime
import com.example.ssu_contest_eighteen_pomise.dto.Pill
import com.example.ssu_contest_eighteen_pomise.network.UserService
import com.example.ssu_contest_eighteen_pomise.sharedpreferences.SettingSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.joda.time.format.DateTimeFormat
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PillManageDetailViewModel(application: Application) : AndroidViewModel(application) {
    var currentPillSetDTO: PillSetDTO? = null
        set(value) {
            field = value
            pillName.value = "약 이름: ${value?.pillName ?: ""}"
            pillCategory.value = "약 종류: ${value?.pillCategory ?: ""}"
            val eatTime =
                PillManageListAdapter.dateConvert(value?.createdAt ?: "2999-12-31 00:00:00")
            pillRegisteredDate.value = "등록일: $eatTime"
            pillExpireDate.value =
                "복용 마감일: ${value?.expireDateYear}-${value?.expireDateMonth}-${value?.expireDateDate}"
        }

    val pillName = MutableLiveData<String>("")
    val pillCategory = MutableLiveData<String>("")
    val pillRegisteredDate = MutableLiveData<String>("등록일: 0000-00-00")
    val pillExpireDate = MutableLiveData<String>("복용 마감일: 0000-00-00")

    val finishEvent = MutableLiveData<Boolean>()
    private val shPre = App.token_prefs
    private val settShrpe = SettingSharedPreferences.setInstance(application)

    val userRetrofit = Retrofit.Builder()
        .baseUrl(UserService.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    val userService = userRetrofit.create(UserService::class.java)

    val registeredTimeList = MutableLiveData<List<PillTime>>()

    fun getRegisteredTimeList() {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                userService.getMyPillRecord(shPre.refreshToken!!)

            if (response.isSuccessful && response.body()!!.isNotEmpty()) {
                val idList = currentPillSetDTO?.id ?: emptyList()
                val set = idList.toMutableSet()
                val tempRegisteredTimePillList = mutableListOf<PillTime>()
                for (p in response.body()!!) {
                    val eatTime = PillManageListAdapter.timeConvert(p.time)
                    if (p.id in set) {
                        tempRegisteredTimePillList.add(PillTime(p.id, eatTime))
                    }
                }

                runBlocking(Dispatchers.Main) {
                    registeredTimeList.value = tempRegisteredTimePillList
                }

            } else {
                // 여기 처리 아직 안함.
            }
        }
    }

    fun deleteTime(position: Int, id: Int) {
        if (id != -1) {
            viewModelScope.launch(Dispatchers.IO) {
                val response = userService.deleteMyPillTime(shPre.refreshToken!!, listOf(id))

                if (response.isSuccessful) {
                    val temp = mutableListOf<PillTime>()
                    for (cur in registeredTimeList.value!!) {
                        if (cur.id != id) {
                            temp.add(
                                PillTime(
                                    cur.id,
                                    SpecificTime(
                                        cur.specificTime.hour,
                                        cur.specificTime.minutes,
                                        cur.specificTime.sec
                                    )
                                )
                            )
                        }
                    }
                    runBlocking(Dispatchers.Main) {
                        registeredTimeList.value=temp
                    }
                } else {

                }
            }
        }
    }

    fun convert(string: String): PillManageViewModel.ExpireDate {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val ldt = LocalDateTime.parse(string, dtf)
            return PillManageViewModel.ExpireDate(ldt.year, ldt.monthValue, ldt.dayOfMonth)
        } else {
            val dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
            val jodatime = dtf.parseDateTime(string)
            return PillManageViewModel.ExpireDate(
                jodatime.year,
                jodatime.monthOfYear,
                jodatime.dayOfMonth
            )
        }
    }

    data class PillTime(
        val id: Int,
        val specificTime: SpecificTime
    )

    fun onClickFinish() {
        finishEvent.value = true
    }
}