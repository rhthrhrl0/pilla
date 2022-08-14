package com.example.ssu_contest_eighteen_pomise.extensionfunction

import android.app.Activity
import com.example.ssu_contest_eighteen_pomise.R

fun Activity.slideUpperAndNone(){
    overridePendingTransition(R.anim.slide_up_enter, R.anim.none)
}
fun Activity.slideNoneAndDownExit(){
    overridePendingTransition(R.anim.none, R.anim.slide_down_exit)
}
fun Activity.slideRightEnterAndNone(){
    overridePendingTransition(R.anim.slide_right_enter, R.anim.none)
}
fun Activity.slideNoneAndLeftExit(){
    overridePendingTransition(R.anim.none, R.anim.slide_right_exit)
}
fun Activity.slideRightEnterAndJustScaleDown(){
    overridePendingTransition(R.anim.slide_right_enter, R.anim.just_scale_down)
}
fun Activity.justScaleUpAndLeftExit(){
    overridePendingTransition(R.anim.just_scale_up, R.anim.slide_right_exit)
}

