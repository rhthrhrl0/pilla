package com.example.ssu_contest_eighteen_pomise.extensionfunction

import android.app.Activity
import android.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ssu_contest_eighteen_pomise.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

fun Activity.showAskDialog(title: String, message: String, onFunc: () -> Unit) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setNegativeButton(R.string.close) { _, _ -> }
        .setPositiveButton(R.string.confirm) { _, _ ->
            onFunc()
        }.setCancelable(false)
        .show()
}

fun Fragment.showAskDialog(title: String, message: String, onFunc: () -> Unit) {
    AlertDialog.Builder(activity)
        .setTitle(title)
        .setMessage(message)
        .setNegativeButton(R.string.close) { _, _ -> }
        .setPositiveButton(R.string.confirm) { _, _ ->
            onFunc()
        }.setCancelable(false)
        .show()
}
