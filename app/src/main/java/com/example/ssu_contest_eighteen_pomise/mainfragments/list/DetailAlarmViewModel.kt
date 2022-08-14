package com.example.ssu_contest_eighteen_pomise.mainfragments.list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ssu_contest_eighteen_pomise.BuildConfig
import com.example.ssu_contest_eighteen_pomise.network.UserService
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


class DetailAlarmViewModel(application: Application) : AndroidViewModel(application) {
    val finishEvent = MutableLiveData<Boolean>()
    var pillAlarmDto: AlarmListDTO? = null

    var gson = GsonBuilder()
        .setLenient()
        .create()

    val userRetrofit = Retrofit.Builder()
        .baseUrl(UserService.PILL_OPEN_SOURCE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    val userService = userRetrofit.create(UserService::class.java)

    fun getPillDetailInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(
                "kmj",
                "${BuildConfig.SAMPLE_API_KEY},${pillAlarmDto?.pillList?.elementAt(0)?.pillName}"
            )

            val br: BufferedReader? = null
            try {
                val urlBuilder =
                    StringBuilder("http://apis.data.go.kr/1471000/DrugPrdtPrmsnInfoService02/getDrugPrdtPrmsnDtlInq01") /*URL*/
                urlBuilder.append(
                    "?" + URLEncoder.encode("ServiceKey", "UTF-8")
                        .toString() + "=" + BuildConfig.SAMPLE_API_KEY
                ) /*Service Key*/
                urlBuilder.append(
                    "&" + URLEncoder.encode("numOfRows", "UTF-8")
                        .toString() + "=" + URLEncoder.encode("3", "UTF-8")
                ) /*한 페이지 결과 수*/
                urlBuilder.append(
                    "&" + URLEncoder.encode("pageNo", "UTF-8")
                        .toString() + "=" + URLEncoder.encode("1", "UTF-8")
                ) /*페이지 번호*/
                urlBuilder.append(
                    "&" + URLEncoder.encode("type", "UTF-8")
                        .toString() + "=" + URLEncoder.encode("xml", "UTF-8")
                ) /*측정소 이름*/
                urlBuilder.append(
                    "&" + URLEncoder.encode("item_name", "UTF-8")
                        .toString() + "=" + URLEncoder.encode(
                        pillAlarmDto!!.pillList.elementAt(0).pillName,
                        "UTF-8"
                    )
                ) /*요청 데이터기간 (하루 : DAILY, 한달 : MONTH, 3달 : 3MONTH)*/

                val url = URL(urlBuilder.toString())

//                val `is`: InputStream = url.openStream()
//                val factory = XmlPullParserFactory.newInstance()
//                val parser = factory.newPullParser()
//                parser.setInput(InputStreamReader(`is`, "UTF-8"))
//
//                var tag: String
//                var eventType = parser.eventType

//                while (eventType != XmlPullParser.END_DOCUMENT) {
//                    when (eventType) {
//                        XmlPullParser.START_DOCUMENT -> {}
//                        XmlPullParser.END_DOCUMENT -> {}
//                        XmlPullParser.END_TAG -> { }
//                        XmlPullParser.START_TAG -> {
//                            if (parser.name == "STORAGE_METHOD") {
//                                //bus = Item()
//                                Log.d("kmj",parser.text)
//                            }
////                            if (parser.name == "locationNo1") b_locationNo1 = true
////                            if (parser.name == "plateNo1") b_plateNo1 = true
////                            if (parser.name == "routeId") b_routeId = true
////                            if (parser.name == "stationId") b_stationId = true
//                        }
////                        XmlPullParser.TEXT -> if (b_locationNo1) {
////                            bus.setLocationNo1(parser.text)
////                            b_locationNo1 = false
////                        } else if (b_plateNo1) {
////                            bus.setPlateNo1(parser.text)
////                            b_plateNo1 = false
////                        } else if (b_routeId) {
////                            bus.setRouteId(parser.text)
////                            b_routeId = false
////                        } else if (b_stationId) {
////                            bus.setStationId(parser.text)
////                            b_stationId = false
////                        }
//                    }
//                    eventType = parser.next()
//                }
                val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.setRequestProperty("Content-type", "application/json")
                System.out.println("Response code: " + conn.responseCode)
                val rd: BufferedReader
                if (conn.responseCode in 200..300) {
                    rd = BufferedReader(InputStreamReader(conn.inputStream))
                    Log.d("kmj", "성공")
                } else {
                    rd = BufferedReader(InputStreamReader(conn.errorStream))
                    Log.d("kmj", "실패")
                }
                val sb = StringBuilder()
                var line: String?
                var countLine=0
                while (rd.readLine().also { line = it } != null) {
                    sb.append(line)
                    Log.d("kmj", "${countLine}번줄 :${line}")
                    countLine++
                }
                rd.close()
                conn.disconnect()
                Log.d("kmj", "$sb")
            } catch (e: Exception) {
                Log.d("kmj", "예외: $e")
            }
        }

    }

    fun onClickFinish() {
        finishEvent.value = true
    }
}