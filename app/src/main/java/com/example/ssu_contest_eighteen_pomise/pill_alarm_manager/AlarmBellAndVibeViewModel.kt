package com.example.ssu_contest_eighteen_pomise.pill_alarm_manager

import android.app.Application
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.alarm.alarm_list_room.AlarmDTO
import com.example.ssu_contest_eighteen_pomise.alarm.alarm_list_room.AlarmDatabase
import com.example.ssu_contest_eighteen_pomise.dto.ReportLaterEatPillNames
import com.example.ssu_contest_eighteen_pomise.sharedpreferences.SettingSharedPreferences
import kotlinx.coroutines.*

class AlarmBellAndVibeViewModel(application: Application) : AndroidViewModel(application) {
    val stopBellLiveData = MutableLiveData<Boolean>()
    val finishEvent = MutableLiveData<Boolean>()
    private val setting_prefs = SettingSharedPreferences.setInstance(application)
    private val db = Room.databaseBuilder(
        application,
        AlarmDatabase::class.java, "database-name-${App.token_prefs.email}"
    ).build()

    val title = MutableLiveData<String>("")
    val body = MutableLiveData<String>("")
    val pillNames = MutableLiveData<String>("")

    val countTimeString = MutableLiveData<String>("180 초 후\n자동으로 보호자에게 알림이 갑니다.")
    private var countTime = 180L
        set(value) {
            field = value
            if (value == 0L) {
                laterEatStopBell()
            }
            countTimeString.value = "${countTime} 초 후\n자동으로 보호자에게 알림이 갑니다."
        }

    val timerJob: Job = viewModelScope.launch(start = CoroutineStart.LAZY) {
        withContext(Dispatchers.IO) {
            var oldTimeMills = System.currentTimeMillis()
            while (countTime > 0L) {
                val delayMills = System.currentTimeMillis() - oldTimeMills
                if (delayMills >= 1000L) {
                    runBlocking(Dispatchers.Main) {
                        countTime -= 1
                    }
                    oldTimeMills = System.currentTimeMillis()
                }
            }
        }
    }

    fun startBell(mediaPlayer: MediaPlayer) {
        viewModelScope.launch {
            mediaPlayer.isLooping = true
            mediaPlayer.start()
        }
    }

    fun stopBell() {
        // 룸 갱신. 해당 알람에 대해서 약 먹었음을 표시.
        viewModelScope.launch(Dispatchers.IO) {
            timerJob.cancel()
            val nowAlarm: AlarmDTO =
                db.alarmDao().getAll().sortedWith(compareBy<AlarmDTO> { it.id }).last()

            db.alarmDao().update(nowAlarm.apply { isRead = true }) // 읽었다고 체크.

            stopBellLiveData.postValue(true)
        }
    }

    fun laterEatStopBell() {
        // 메시지 보내기.
        viewModelScope.launch(Dispatchers.IO) {
            timerJob.cancel()
            val response = App.userService.laterEatPostToGuardian(
                App.token_prefs.refreshToken!!,
                ReportLaterEatPillNames(pillNames.value!!)
            )
            if (response.isSuccessful) {

            } else {
                runBlocking(Dispatchers.Main) {
                    Toast.makeText(getApplication(), "토큰 만료. 다시 로그인해주세요.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            stopBellLiveData.postValue(true)
        }
    }


    //최종적으로 거쳐감.
    fun finishEvent() {
        val finishJob = viewModelScope.launch {
            delay(1000L)
            finishEvent.value = true
        }
    }

    fun startVibe(vibe: Vibrator) {
        viewModelScope.launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibe.vibrate(VibrationEffect.createWaveform(AlarmBellAndVibeActivity.pattern, 0))
            } else {
                vibe.vibrate(AlarmBellAndVibeActivity.pattern, 0)
            }
        }
    }

    companion object {
        const val TAIL_STRING = "(정)을 먹을 시간 입니다."
    }
}