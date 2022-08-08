package com.example.ssu_contest_eighteen_pomise.pill_alarm_manager

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.sharedpreferences.TokenSharedPreferences

class PillAlarmReceiver : BroadcastReceiver() {
    private lateinit var shPre:TokenSharedPreferences
    private lateinit var db:RoomDatabase
    private lateinit var notificationManager: NotificationManager
    // 알람매니저에게 요청한 시간이 되면 이 브로드캐스트 리시버의 이 함수로
    // 내가 pendingIntent에 담았던 일반 인텐트를 이 함수로 넘겨주면서 함께 이벤트를 발생시켜줄거임.
    override fun onReceive(context: Context, intent: Intent) {
        //노티피케이션 만들어서 앱 상단에 알림 띄울 디자인 설계
        //그리고 노티피케이션 클릭 시 발생할 펜딩인텐트 설정해주고 띄우기.

        shPre= App.token_prefs

        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        // 룸 안에서 실행해야 할 시간 중에서 현재 시간 이후 가장 작은 시간을 찾아서 알람매니저에게 요청하기.
    }


}