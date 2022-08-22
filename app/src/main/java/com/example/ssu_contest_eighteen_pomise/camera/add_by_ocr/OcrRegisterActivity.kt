package com.example.ssu_contest_eighteen_pomise.camera.add_by_ocr

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.AddSelfNoOcrActivity
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.PillNameCategoryAdapter
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.SpecificTimeAddAdapter
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityOcrRegisterBinding
import com.example.ssu_contest_eighteen_pomise.dto.OcrAndImageData
import com.google.android.material.datepicker.MaterialDatePicker
import com.yourssu.design.system.atom.Picker
import com.yourssu.design.system.foundation.Typo
import com.yourssu.design.system.language.bottomSheet
import com.yourssu.design.system.language.picker
import com.yourssu.design.system.language.setLayout
import com.yourssu.design.system.language.text
import com.yourssu.design.undercarriage.size.dpToIntPx
import java.util.*

class OcrRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOcrRegisterBinding
    private val viewModel: AddRegisterOcrViewModel by viewModels()

    val ocrPillSetAdapter = OcrPillSetListAdapter()
    val specificTimeAddadapter = SpecificTimeAddAdapter()
    val pillNameCategoryAdapter = PillNameCategoryAdapter()

    private val onHopeStartTimeValueChangeListener = object : Picker.OnValueChangeListener {
        override fun onValueChange(
            firstValue: String,
            secondValue: String,
            thirdValue: String,
            totalValue: String,
        ) {
            viewModel.startTimeInt = AddSelfNoOcrActivity.timeList.indexOf(firstValue)
            viewModel.cycleTimeInt = 0
            binding.periodicTime.setOnClickListener {
                bottomSheet {
                    text {
                        text = "시간 주기"
                        typo = Typo.SubTitle2

                        setLayout(leftMarginPx = context.dpToIntPx(16f))
                    }
                    picker {
                        setFirstRow(AddSelfNoOcrActivity.timeList.slice(0..(23 - viewModel.startTimeInt)))
                        if (((22 - viewModel.startTimeInt) / 2) == 0) {
                            setFirstRowPosition(0)
                        } else {
                            setFirstRowPosition((22 - viewModel.startTimeInt) / 2)
                        }
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
            viewModel.cycleTimeInt = AddSelfNoOcrActivity.timeList.indexOf(firstValue)
        }
    }

    private val onHopeMorningEatTimeValueChangeListener = object : Picker.OnValueChangeListener {
        override fun onValueChange(
            firstValue: String,
            secondValue: String,
            thirdValue: String,
            totalValue: String,
        ) {
            viewModel.morningEatTime = firstValue
        }
    }

    private val onHopeLunchEatTimeValueChangeListener = object : Picker.OnValueChangeListener {
        override fun onValueChange(
            firstValue: String,
            secondValue: String,
            thirdValue: String,
            totalValue: String,
        ) {
            viewModel.lunchEatTime = firstValue
        }
    }

    private val onHopeDinnerEatTimeValueChangeListener = object : Picker.OnValueChangeListener {
        override fun onValueChange(
            firstValue: String,
            secondValue: String,
            thirdValue: String,
            totalValue: String,
        ) {
            viewModel.dinnerEatTime = firstValue
        }
    }

    private val onSpecificHourTimeValueChangeListener = object : Picker.OnValueChangeListener {
        override fun onValueChange(
            firstValue: String,
            secondValue: String,
            thirdValue: String,
            totalValue: String,
        ) {
            viewModel.specificTimeHourInt = AddSelfNoOcrActivity.timeList.indexOf(firstValue)
        }
    }

    private val onSpecificMinutesTimeValueChangeListener = object : Picker.OnValueChangeListener {
        override fun onValueChange(
            firstValue: String,
            secondValue: String,
            thirdValue: String,
            totalValue: String,
        ) {
            viewModel.specificTimeMinutesInt =
                AddSelfNoOcrActivity.minutesList.indexOf(firstValue) * 5
        }
    }

    private val onPillCategoryChangeListener = object : Picker.OnValueChangeListener {
        override fun onValueChange(
            firstValue: String,
            secondValue: String,
            thirdValue: String,
            totalValue: String,
        ) {
            viewModel.pillCategoryInt = AddSelfNoOcrActivity.pillCategoryList.indexOf(firstValue)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ocr_register)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.ocrImageDataInit =
            intent.getSerializableExtra(KEY_OCR_DATA_NAME) as OcrAndImageData
        onViewModelInit()
        initView()

        viewModel.classifySet()
    }

    fun onViewModelInit() {
        viewModel.finishEvent.observe(this, {
            onBackPressed()
        })

        viewModel.addPrescriptionEvent.observe(this, {
            Toast.makeText(this, "성공적으로 처방내용이 추가됐습니다", Toast.LENGTH_SHORT).show()
        })

//        viewModel.failedAddPrescriptionEvent.observe(this, {
//            Toast.makeText(this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show()
//        })

        viewModel.pillSetList.observe(this, {
            ocrPillSetAdapter.submitList(it)
        })

        viewModel.specificTimeItemLiveData.observe(this, {
            specificTimeAddadapter.updateItems(it) //여기 안에 notifyDataSetChanged 있음.
        })

        viewModel.pillNameCategoryListLiveData.observe(this, {
            pillNameCategoryAdapter.updateItems(it)
        })

//        viewModel.addErrorString.observe(this, {
//            Toast.makeText(this, viewModel.addErrorString.value?.reason, Toast.LENGTH_SHORT).show()
//        })
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
                    setFirstRow(AddSelfNoOcrActivity.timeList.slice(1..22))
                    setFirstRowPosition(11) //timeList를 슬라이스해서 넘겨진 배열은 1시~23시의 배열임. 여기서 11번째 인덱스가 12시임.
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
                    setFirstRow(AddSelfNoOcrActivity.eatPillTimeList)
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
                    setFirstRow(AddSelfNoOcrActivity.eatPillTimeList)
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
                    setFirstRow(AddSelfNoOcrActivity.eatPillTimeList)
                    setFirstRowPosition(0)
                    this.onValueChangeListener = onHopeDinnerEatTimeValueChangeListener
                }
            }
        }

        binding.dateEditBtn.setOnClickListener {
            val mcurrentTime = Calendar.getInstance()
            val year = mcurrentTime.get(Calendar.YEAR)
            val month = mcurrentTime.get(Calendar.MONTH)
            val day = mcurrentTime.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                R.style.MySpinnerDatePickerStyle,
                object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(
                        view: DatePicker?,
                        year: Int,
                        month: Int,
                        dayOfMonth: Int
                    ) {
                        //selectedDate.setText(String.format("%d / %d / %d", dayOfMonth, month + 1, year))
                        viewModel.expirationYearInt = year
                        viewModel.expirationMonthInt = month + 1
                        viewModel.expirationDayInt = dayOfMonth
                    }
                },
                year,
                month,
                day
            ).also {
                it.datePicker.minDate = System.currentTimeMillis() - 1000
            }
            datePicker.show()
            //datePicker.updateDate(2028,10,25)
            datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(resources.getColor(R.color.mainColor, theme))
            datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setTextColor(resources.getColor(R.color.mainColor, theme))
        }

        binding.specificHourBtn.setOnClickListener {
            bottomSheet {
                text {
                    text = "시"
                    typo = Typo.SubTitle2

                    setLayout(leftMarginPx = context.dpToIntPx(16f))
                }
                picker {
                    setFirstRow(AddSelfNoOcrActivity.timeList)
                    setFirstRowPosition(0)
                    this.onValueChangeListener = onSpecificHourTimeValueChangeListener
                }
            }
        }

        binding.specificMinutesBtn.setOnClickListener {
            bottomSheet {
                text {
                    text = "분"
                    typo = Typo.SubTitle2

                    setLayout(leftMarginPx = context.dpToIntPx(16f))
                }
                picker {
                    setFirstRow(AddSelfNoOcrActivity.minutesList)
                    setFirstRowPosition(0)
                    this.onValueChangeListener = onSpecificMinutesTimeValueChangeListener
                }
            }
        }

        binding.specificTimeRecyclerView.apply {
            this.layoutManager = GridLayoutManager(applicationContext, 3)
            this.adapter = specificTimeAddadapter
        }
        specificTimeAddadapter.setMyItemClickListener(object :
            SpecificTimeAddAdapter.MyItemClickListener {
            override fun onItemClick(position: Int) {
                viewModel.specificTimeItemOnClick(position)
            }

            override fun onItemLongClick(position: Int) {

            }
        })

        binding.pillCategoryBtn.setOnClickListener {
            bottomSheet {
                text {
                    text = "약 종류"
                    typo = Typo.SubTitle2

                    setLayout(leftMarginPx = context.dpToIntPx(16f))
                }
                picker {
                    setFirstRow(AddSelfNoOcrActivity.pillCategoryList)
                    setFirstRowPosition(0)
                    this.onValueChangeListener = onPillCategoryChangeListener
                }
            }
        }

        binding.pillSetRv.apply {
            this.layoutManager =
                LinearLayoutManager(this@OcrRegisterActivity, LinearLayoutManager.HORIZONTAL, false)

            this.adapter = ocrPillSetAdapter
            ocrPillSetAdapter.setMyItemClickListener(object :
                OcrPillSetListAdapter.MyItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    if (viewModel.curIndex != position) {

                    }
                    viewModel.curIndex = position
                }
            })
        }

        binding.specificPillRecyclerView.apply {
            this.layoutManager =
                LinearLayoutManager(this@OcrRegisterActivity, LinearLayoutManager.VERTICAL, false)

            this.adapter = pillNameCategoryAdapter
        }
        pillNameCategoryAdapter.setMyItemClickListener(object :
            PillNameCategoryAdapter.MyItemClickListener {
            override fun onItemDeleteClick(position: Int) {
                viewModel.pillNameCategoryOnDeleteClick(position)
            }

            override fun onItemClick(position: Int) {

            }

            override fun onItemLongClick(position: Int) {

            }

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //slideNoneAndDownExit()
    }

    companion object {
        const val KEY_OCR_DATA_NAME = "key_ocr_data_name_"
    }
}