package com.example.ssu_contest_eighteen_pomise.mainfragments.list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ssu_contest_eighteen_pomise.BuildConfig
import com.example.ssu_contest_eighteen_pomise.dto.PillAlarmDetail
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
import kotlin.system.exitProcess


class DetailAlarmViewModel(application: Application) : AndroidViewModel(application) {
    val finishEvent = MutableLiveData<Boolean>()
    var pillAlarmDto: AlarmListDTO? = null
//    lateinit var pillList:ArrayList<PillAlarmDetail>
    //list를 감싼 MutableLiveData는 observe에 변화를 알려주지 않음
    var pillList = MutableLiveData<ArrayList<PillAlarmDetail>>()

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

            var threadPillList:ArrayList<PillAlarmDetail> = ArrayList() //백그라운드 postValue를 위해서서

            val len = pillAlarmDto?.pillList?.size?.minus(1) //알람에있는 약 개체수
            for (elementNum: Int in 0..len!!) {

                Log.d(
                    "kmj",
                    "${BuildConfig.SAMPLE_API_KEY},${pillAlarmDto?.pillList?.elementAt(elementNum)?.pillName}"
                )

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
                            pillAlarmDto!!.pillList.elementAt(elementNum).pillName,
                            "UTF-8"
                        )
                    ) /*요청 데이터기간 (하루 : DAILY, 한달 : MONTH, 3달 : 3MONTH)*/

                    val url = URL(urlBuilder.toString())

                    val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                    conn.requestMethod = "GET"
                    conn.setRequestProperty("Content-type", "application/json")
                    System.out.println("Response code: " + conn.responseCode)
                    val rd: BufferedReader
                    //connection에서 읽어올 br 준비
                    if (conn.responseCode in 200..300) {
                        rd = BufferedReader(InputStreamReader(conn.inputStream))
                        Log.d("kmj", "성공")
                    } else {
                        rd = BufferedReader(InputStreamReader(conn.errorStream))
                        Log.d("kmj", "실패")
//                        finishEvent.value = true
                    }
                    val sb = StringBuilder()
                    var line: String?
                    var countLine = 0
                    while (rd.readLine().also { line = it } != null) {
                        sb.append(line)
//                    Log.d("kmj", "${countLine}번줄 :${line}")
                        countLine++
                    }
                    rd.close()
                    conn.disconnect()
//                    Log.d("kmj", "$sb")

                    var parserFactory = XmlPullParserFactory.newInstance()
                    var parser = parserFactory.newPullParser()
                    var inputStream = url.openStream()
                    parser.setInput(inputStream, "UTF-8")
                    var eventType = parser.eventType
                    var item_name = false
                    var entp_name = false
                    var chart = false
                    var ee_doc_data = false
//                var ud_doc_data = false
                    var paragraph = false

                    var sb_name = StringBuilder()
                    var sb_entp = StringBuilder()
                    var sb_chart = StringBuilder()
                    var sb_ee = StringBuilder()

                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        var name: String? = null
                        //첫 번째 정보만 받아옴
                        if (eventType == XmlPullParser.END_TAG) {
                            name = parser.name
                            if (name.equals("item")) {
                                Log.d("kyb", "파싱 종료")
                                break
                            }
                        } else if (eventType == XmlPullParser.START_TAG) {
                            name = parser.name
                            //약명
                            if (name.equals("ITEM_NAME")) {
                                item_name = true
                            }
                            //제조사
                            else if (name.equals("ENTP_NAME")) {
                                entp_name = true
                            }
                            //생김새
                            else if (name.equals("CHART")) {
                                chart = true
                            }
                            //효능, 효과
                            else if (name.equals("EE_DOC_DATA")) {
                                ee_doc_data = true
                            }
                            //용법, 용량
//                        else if(name.equals("UD_DOC_DATA")) {
//                            ud_doc_data = true
//                        }
                        } else if (eventType == XmlPullParser.TEXT) {
                            if (item_name) {
                                Log.d("drug_name", parser.text)
                                sb_name.append(parser.text)
                                item_name = false
                            } else if (entp_name) {
                                Log.d("drug_entp", parser.text)
                                sb_entp.append(parser.text)
                                entp_name = false
                            } else if (chart) {
                                Log.d("drug_chart", parser.text)
                                sb_chart.append(parser.text)
                                chart = false
                            } else if (ee_doc_data) {
                                ee_doc_data = false
                                while (eventType != XmlPullParser.END_DOCUMENT) {
                                    if (eventType == XmlPullParser.END_TAG) {
                                        if (parser.name.equals("EE_DOC_DATA"))
                                            break
                                    } else if (eventType == XmlPullParser.START_TAG && (parser.name.equals("PARAGRAPH"))) {
                                        paragraph = true
                                    } else if (eventType == XmlPullParser.TEXT && paragraph) {
                                        if (!parser.text.contains("<")) {
                                            Log.d("drug_ee", parser.text)
                                            sb_ee.append(parser.text)
                                        }
                                        paragraph = false
                                    }
                                    eventType = parser.next()
                                }
                            }
//                        else if(ud_doc_data) {
//                            ud_doc_data = false
//                            while(eventType!=XmlPullParser.END_DOCUMENT) {
//                                if(eventType==XmlPullParser.END_TAG) {
//                                    if(parser.name.equals("UD_DOC_DATA"))
//                                        break
//                                }
//                                else if(eventType==XmlPullParser.START_TAG && parser.name.equals("PARAGRAPH")) {
//                                    paragraph = true
//                                }
//                                else if(eventType==XmlPullParser.TEXT && paragraph) {
//                                    if(!parser.text.contains("<"))
//                                        Log.d("kyb6", parser.text)
//                                    paragraph = false
//                                }
//                                eventType = parser.next()
//                            }
//                        }
                        }

                        eventType = parser.next()

                    } //while 종료

                    //리스트에 약 추가
                    threadPillList.add(PillAlarmDetail(sb_name.toString(), sb_entp.toString(), sb_chart.toString(), sb_ee.toString()))
                    inputStream.close()

                } catch (e: Exception) {
                    Log.d("kmj", "예외: $e")
                }
            } //for문 종료
            pillList.postValue(threadPillList)
            Log.d("kybkyb", threadPillList.size.toString())
        }
    }

    fun onClickFinish() {
        finishEvent.value = true
    }
}