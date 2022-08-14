package com.example.ssu_contest_eighteen_pomise.pill_alarm_manager

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.PowerManager
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class AlarmFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        const val TAG = "kmj"
        const val NOTIFICATION_ID = 0
        const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
        const val title = "title"
        const val body = "body"
    }

    lateinit var notificationManager: NotificationManager

    // 토큰은 앱을 삭제하거나 앱의 내부데이터를 삭제하는게 아닌 이상 재발급 되지 않음. 매니페스트에서 메타데이터로 주기적발급 막아놓음.
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("kmj", "Refreshed token: $token")
        val shPre = App.token_prefs
        shPre.fcmToken = token // 쉐어드 프리퍼런스 내부 저장소에 토큰 저장.
    }

    override fun onMessageReceived(message: RemoteMessage) {
        //푸시울렸을때 화면깨우기.
        //푸시울렸을때 화면깨우기.
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        @SuppressLint("InvalidWakeLockTag") val wakeLock = pm.newWakeLock(
            PowerManager.SCREEN_DIM_WAKE_LOCK
                    or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "TAG"
        )
        wakeLock.acquire(5000)

        Log.d("kmj", "알림옴.${message}")
        notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel()
        deliverNotification(this, message.data[title], message.data[body])

        val alarmIntent = Intent(this, AlarmBellAndVibeActivity::class.java)
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(alarmIntent)
        wakeLock.release()
    }

    private fun deliverNotification(context: Context, title: String? = "", body: String? = "") {
        val contentIntent = Intent(context, SplashActivity::class.java)
        contentIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_MUTABLE
        )
        val builder =
            NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Stand up notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "AlarmManager Tests"
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}