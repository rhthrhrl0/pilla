package com.example.ssu_contest_eighteen_pomise.pill_alarm_manager

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityAlarmBellAndVibeBinding


class AlarmBellAndVibeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBellAndVibeBinding
    private val viewModel: AlarmBellAndVibeViewModel by viewModels()
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibe:Vibrator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm_bell_and_vibe)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // 화면 오버레이를 위해. 이게 있어야 잠금화면 위로도 알람액티비티가 뜬다.
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        )
        mediaPlayer = MediaPlayer.create(this, R.raw.be_happy)
        vibe = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibe.vibrate(pattern, 0)

        viewModel.startBell(mediaPlayer)
        onViewModelInit()
    }

    fun onViewModelInit() {
        viewModel.stopBellLiveData.observe(this, {
            mediaPlayer.stop()
            vibe.cancel()
            binding.lottieWatchView.cancelAnimation()
            viewModel.finishEvent()
        })

        viewModel.finishEvent.observe(this,{
            finishAndRemoveTask()
        })
    }

    override fun onBackPressed() {}

    companion object{
        val pattern = longArrayOf(0,400,800,600,800,800,800,1000)
    }
}