package com.example.ssu_contest_eighteen_pomise.pill_alarm_manager

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ssu_contest_eighteen_pomise.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AlarmBellAndVibeViewModel(application: Application) : AndroidViewModel(application) {
    val stopBellLiveData = MutableLiveData<Boolean>()
    val finishEvent = MutableLiveData<Boolean>()

    fun startBell(mediaPlayer: MediaPlayer) {
        viewModelScope.launch {
            mediaPlayer.isLooping = true
            mediaPlayer.start()
        }
    }

    fun stopBell() {
        // 룸 갱신. 해당 알람에 대해서 약 먹었음을 표시.
        stopBellLiveData.value = true
    }

    fun laterEatStopBell(){
        // 메시지 보내기.
        stopBellLiveData.value=true
    }

    fun finishEvent(){
        val finishJob=viewModelScope.launch {
            delay(1000L)
            finishEvent.value=true
        }
    }

}