package com.example.ssu_contest_eighteen_pomise.alarm.alarm_list_room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlarmDTO(
    val title: String,
    val body: String,
    val receivedTime: String, // 받은 날짜 및 시간.
    var isRead: Boolean // 읽었는지 유무.
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
