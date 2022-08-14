package com.example.ssu_contest_eighteen_pomise

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock.sleep
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.ssu_contest_eighteen_pomise.auth.LoginActivity
import com.example.ssu_contest_eighteen_pomise.network.LoginService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class SplashActivity : AppCompatActivity() {
    private val viewModel: SplashViewModel by viewModels()
    private val backKeyHandler = BackKeyHandler(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val shPre = App.token_prefs

        viewModel.canLogin.observe(this@SplashActivity, {
            sleep(500)
            AutoLogin()
        })

        viewModel.canNotLogin.observe(this@SplashActivity, {
            sleep(500)
            HaveToLogin()
        })

        viewModel.isCanLogin(shPre)
    }

    override fun onBackPressed() {
        backKeyHandler.onBackPressed()
    }

    fun AutoLogin() {
        checkPermissionOverlay(AutoLoginForOverlay)
    }

    fun HaveToLogin() {
        checkPermissionOverlay(HaveToLoginForOverlay)
    }

    fun checkPermissionOverlay(code: Int){
        //마시멜로우 이상 버전에서는 화면 오버레이 권한을 얻기 위한 설정창으로 이동해서 사용자가 직접 켜야 함.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + BuildConfig.APPLICATION_ID)
            )
            startActivityForResult(intent, code)
        }
        else{
            startActivityNextStep(code)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var isCanUseApp:Boolean=false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(Settings.canDrawOverlays(this)){
                isCanUseApp=true
                Toast.makeText(this,"권한부여성공", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this,"권한 부여 실패\n앱을 종료합니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        startActivityNextStep(resultCode)
    }

    fun startActivityNextStep(requestCode:Int){
        when(requestCode){
            AutoLoginForOverlay->{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            HaveToLoginForOverlay->{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    companion object{
        const val AutoLoginForOverlay=1
        const val HaveToLoginForOverlay=2
    }
}