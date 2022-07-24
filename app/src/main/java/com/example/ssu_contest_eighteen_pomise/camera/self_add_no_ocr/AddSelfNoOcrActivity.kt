package com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityAddSelfNoOcrBinding
import com.example.ssu_contest_eighteen_pomise.extensionfunction.slideNoneAndDownExit
import com.yourssu.design.system.atom.ToolTip

class AddSelfNoOcrActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddSelfNoOcrBinding
    private val viewModel: AddSelfNoOcrViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_self_no_ocr)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        onViewModelInit()


    }

    fun onViewModelInit() {
        viewModel.finishEvent.observe(this@AddSelfNoOcrActivity, {
            onBackPressed()
        })

        viewModel.addPrescriptionEvent.observe(this@AddSelfNoOcrActivity, {

        })

        viewModel.failedAddPrescriptionEvent.observe(this@AddSelfNoOcrActivity, {
            Toast.makeText(this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show()
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //slideNoneAndDownExit()
    }

    companion object {
        val startTimeList = listOf<Pair<String, Int>>(
            "1시부터" to 1,
            "2시부터" to 2,
            "3시부터" to 3,
            "4시부터" to 4,
            "5시부터" to 5,
            "6시부터" to 6,
            "7시부터" to 7,
            "8시부터" to 8,
            "9시부터" to 9,
            "10시부터" to 10,
            "11시부터" to 11,
            "12시부터" to 12,
            "13시부터" to 13,
            "14시부터" to 14,
            "15시부터" to 15,
            "16시부터" to 16,
            "17시부터" to 17,
            "18시부터" to 18,
            "19시부터" to 19,
            "20시부터" to 20,
            "21시부터" to 21,
            "22시부터" to 22,
            "23시부터" to 23,
            "0시부터" to 0
        )
    }
}