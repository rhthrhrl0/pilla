package com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityAddSelfNoOcrBinding
import com.example.ssu_contest_eighteen_pomise.extensionfunction.slideNoneAndDownExit
import com.yourssu.design.system.atom.Picker
import com.yourssu.design.system.atom.ToolTip
import com.yourssu.design.system.foundation.Typo
import com.yourssu.design.system.language.bottomSheet
import com.yourssu.design.system.language.picker
import com.yourssu.design.system.language.setLayout
import com.yourssu.design.system.language.text
import com.yourssu.design.undercarriage.size.dpToIntPx

class AddSelfNoOcrActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddSelfNoOcrBinding
    private val viewModel: AddSelfNoOcrViewModel by viewModels()

    private val onHopeStartTimeValueChangeListener = object : Picker.OnValueChangeListener {
        override fun onValueChange(
            firstValue: String,
            secondValue: String,
            thirdValue: String,
            totalValue: String,
        ) {
            viewModel.startTimeInt = timeList.indexOf(firstValue)
            viewModel.cycleTimeInt = 0
            binding.periodicTime.setOnClickListener {
                bottomSheet {
                    text {
                        text = "시간 주기"
                        typo = Typo.SubTitle2

                        setLayout(leftMarginPx = context.dpToIntPx(16f))
                    }
                    picker {
                        setFirstRow(timeList.slice(0..(23 - viewModel.startTimeInt)))
                        setFirstRowPosition(timeList.indexOf("0"))
                        this.onValueChangeListener = onHopeCycleTimeValueChangeListener
                    }
                }
            }
        }
    }

    private val onHopeCycleTimeValueChangeListener = object : Picker.OnValueChangeListener {
        override fun onValueChange(
            firstValue: String,
            secondValue: String,
            thirdValue: String,
            totalValue: String,
        ) {
            viewModel.cycleTimeInt = timeList.indexOf(firstValue)
        }
    }

    private val onHopeMorningEatTimeValueChangeListener = object : Picker.OnValueChangeListener {
        override fun onValueChange(
            firstValue: String,
            secondValue: String,
            thirdValue: String,
            totalValue: String,
        ) {
            viewModel.morningEatTimeString.value = firstValue
        }
    }

    private val onHopeLunchEatTimeValueChangeListener = object : Picker.OnValueChangeListener {
        override fun onValueChange(
            firstValue: String,
            secondValue: String,
            thirdValue: String,
            totalValue: String,
        ) {
            viewModel.lunchEatTimeString.value = firstValue
        }
    }

    private val onHopeDinnerEatTimeValueChangeListener = object : Picker.OnValueChangeListener {
        override fun onValueChange(
            firstValue: String,
            secondValue: String,
            thirdValue: String,
            totalValue: String,
        ) {
            viewModel.dinnerEatTimeString.value = firstValue
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_self_no_ocr)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        onViewModelInit()

        initView()

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

    fun initView() {
        binding.startTime.setOnClickListener {
            bottomSheet {
                text {
                    text = "시작시간"
                    typo = Typo.SubTitle2

                    setLayout(leftMarginPx = context.dpToIntPx(16f))
                }
                picker {
                    setFirstRow(timeList)
                    setFirstRowPosition(timeList.indexOf(viewModel.startTimeString.value))
                    this.onValueChangeListener = onHopeStartTimeValueChangeListener
                }
            }
        }

        binding.morningBtn.setOnClickListener {
            bottomSheet {
                text {
                    text = "아침약 복용 시간"
                    typo = Typo.SubTitle2

                    setLayout(leftMarginPx = context.dpToIntPx(16f))
                }
                picker {
                    setFirstRow(eatPillTimeList)
                    setFirstRowPosition(0)
                    this.onValueChangeListener = onHopeMorningEatTimeValueChangeListener
                }
            }
        }

        binding.lunchBtn.setOnClickListener {
            bottomSheet {
                text {
                    text = "점심약 복용 시간"
                    typo = Typo.SubTitle2

                    setLayout(leftMarginPx = context.dpToIntPx(16f))
                }
                picker {
                    setFirstRow(eatPillTimeList)
                    setFirstRowPosition(0)
                    this.onValueChangeListener = onHopeLunchEatTimeValueChangeListener
                }
            }
        }

        binding.dinnerBtn.setOnClickListener {
            bottomSheet {
                text {
                    text = "저녁약 복용 시간"
                    typo = Typo.SubTitle2

                    setLayout(leftMarginPx = context.dpToIntPx(16f))
                }
                picker {
                    setFirstRow(eatPillTimeList)
                    setFirstRowPosition(0)
                    this.onValueChangeListener = onHopeDinnerEatTimeValueChangeListener
                }
            }
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        //slideNoneAndDownExit()
    }

    companion object {
        val timeStringIntList = listOf<Pair<String, Int>>(
            "0" to 0,
            "1" to 1,
            "2" to 2,
            "3" to 3,
            "4" to 4,
            "5" to 5,
            "6" to 6,
            "7" to 7,
            "8" to 8,
            "9" to 9,
            "10" to 10,
            "11" to 11,
            "12" to 12,
            "13" to 13,
            "14" to 14,
            "15" to 15,
            "16" to 16,
            "17" to 17,
            "18" to 18,
            "19" to 19,
            "20" to 20,
            "21" to 21,
            "22" to 22,
            "23" to 23
        )

        val timeList = listOf<String>(
            timeStringIntList[0].first,
            timeStringIntList[1].first,
            timeStringIntList[2].first,
            timeStringIntList[3].first,
            timeStringIntList[4].first,
            timeStringIntList[5].first,
            timeStringIntList[6].first,
            timeStringIntList[7].first,
            timeStringIntList[8].first,
            timeStringIntList[9].first,
            timeStringIntList[10].first,
            timeStringIntList[11].first,
            timeStringIntList[12].first,
            timeStringIntList[13].first,
            timeStringIntList[14].first,
            timeStringIntList[15].first,
            timeStringIntList[16].first,
            timeStringIntList[17].first,
            timeStringIntList[18].first,
            timeStringIntList[19].first,
            timeStringIntList[20].first,
            timeStringIntList[21].first,
            timeStringIntList[22].first,
            timeStringIntList[23].first
        )

        val eatPillTimeList = listOf<String>(
            "복용 안함",
            "식전 30분",
            "식후 30분",
            "식사 할때"
        )
    }
}