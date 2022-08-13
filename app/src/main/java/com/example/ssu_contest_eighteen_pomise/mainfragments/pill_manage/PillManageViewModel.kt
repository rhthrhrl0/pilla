package com.example.ssu_contest_eighteen_pomise.mainfragments.pill_manage

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.SpecificTime
import com.example.ssu_contest_eighteen_pomise.network.UserService
import com.example.ssu_contest_eighteen_pomise.sharedpreferences.SettingSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.joda.time.format.DateTimeFormat
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PillManageViewModel(application: Application) : AndroidViewModel(application) {
    val finishEvent = MutableLiveData<Boolean>()
    private val shPre = App.token_prefs
    private val settShrpe = SettingSharedPreferences.setInstance(application)

    val userRetrofit = Retrofit.Builder()
        .baseUrl(UserService.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    val userService = userRetrofit.create(UserService::class.java)

    val registeredPillList = MutableLiveData<List<PillSetDTO>>()

    init {
        getRegisteredPillList()
    }

    fun getRegisteredPillList() {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                userService.getMyPillRecord(shPre.refreshToken!!)

            if (response.isSuccessful && response.body()!!.isNotEmpty()) {
                val set = mutableSetOf<TempPillSet>()
                for (p in response.body()!!) {
                    val expireTime = convert(p.time)
                    set.add(
                        TempPillSet(
                            p.createdAt,
                            p.pillCategory,
                            p.pillName,
                            expireTime.expireDateYear,
                            expireTime.expireDateMonth,
                            expireTime.expireDateDate
                        )
                    )
                }

                val tempListOfSet = set.toMutableList()
                val tempList = mutableListOf<PillSetDTO>()
                for (tl in tempListOfSet) {
                    tempList.add(
                        PillSetDTO(
                            tl.createdAt,
                            mutableListOf<Int>(),
                            tl.pillCategory,
                            tl.pillName,
                            mutableListOf<SpecificTime>(),
                            tl.expireDateYear,
                            tl.expireDateMonth,
                            tl.expireDateDate
                        )
                    )
                }

                for (p in response.body()!!) {
                    val expireTime = convert(p.time)

                    for (tle in tempList) {
                        if (tle.createdAt == p.createdAt && tle.pillCategory == p.pillCategory &&
                            tle.pillName == p.pillName && tle.expireDateYear == expireTime.expireDateYear &&
                            tle.expireDateMonth == expireTime.expireDateMonth &&
                            tle.expireDateDate == expireTime.expireDateDate
                        ) {
                            tle.id.add(p.id)
                            tle.time.add(PillManageListAdapter.timeConvert(p.time))
                        }
                    }
                }

                runBlocking(Dispatchers.Main) {
                    registeredPillList.value=tempList
                }

            } else {

            }
        }
    }

    fun onClickFinish() {
        finishEvent.value = true
    }

    data class TempPillSet(
        val createdAt: String,
        val pillCategory: String,
        val pillName: String,
        val expireDateYear: Int,
        val expireDateMonth: Int,
        val expireDateDate: Int
    )

    data class ExpireDate(
        val expireDateYear: Int,
        val expireDateMonth: Int,
        val expireDateDate: Int
    )

    fun convert(string: String): ExpireDate {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val ldt = LocalDateTime.parse(string, dtf)
            return ExpireDate(ldt.year, ldt.monthValue, ldt.dayOfMonth)
        } else {
            val dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
            val jodatime = dtf.parseDateTime(string)
            return ExpireDate(jodatime.year, jodatime.monthOfYear, jodatime.dayOfMonth)
        }
    }
}