package com.example.ssu_contest_eighteen_pomise

import android.app.Activity
import android.util.Log
import android.widget.Toast

class BackKeyHandler(val activity: Activity) {
    var backPressedTime = 0L
    lateinit var toast:Toast
    fun onBackPressed() {
        if (System.currentTimeMillis() > (backPressedTime + 2000)) {
            backPressedTime = System.currentTimeMillis()
            toast=Toast.makeText(activity, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다", Toast.LENGTH_SHORT)
            toast.show()
        }
        else if (System.currentTimeMillis() <= (backPressedTime+2000)){
            activity.finish()
            toast.cancel()
        }
    }
}