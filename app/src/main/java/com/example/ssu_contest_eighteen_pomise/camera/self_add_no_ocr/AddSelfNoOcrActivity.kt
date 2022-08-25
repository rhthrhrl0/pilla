package com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityAddSelfNoOcrBinding
import com.example.ssu_contest_eighteen_pomise.extensionfunction.showAskDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.yourssu.design.system.atom.Picker
import com.yourssu.design.system.component.Toast.Companion.shortToast
import com.yourssu.design.system.foundation.Typo
import com.yourssu.design.system.language.bottomSheet
import com.yourssu.design.system.language.picker
import com.yourssu.design.system.language.setLayout
import com.yourssu.design.system.language.text
import com.yourssu.design.undercarriage.size.dpToIntPx
import java.util.*

class AddSelfNoOcrActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddSelfNoOcrBinding
    private val viewModel: AddSelfNoOcrViewModel by viewModels()
    private val builder = MaterialDatePicker.Builder.datePicker()
        .also {
            title = "Pick Date"
        }

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

    private val onSpecificHourTimeValueChangeListener = object : Picker.OnValueChangeListener {
        override fun onValueChange(
            firstValue: String,
            secondValue: String,
            thirdValue: String,
            totalValue: String,
        ) {
            viewModel.specificTimeHourInt = timeList.indexOf(firstValue)
        }
    }

    private val onSpecificMunutesTimeValueChangeListener = object : Picker.OnValueChangeListener {
        override fun onValueChange(
            firstValue: String,
            secondValue: String,
            thirdValue: String,
            totalValue: String,
        ) {
            viewModel.specificTimeMinutesInt = minutesList.indexOf(firstValue) * 5
        }
    }

    private val onPillCategoryChangeListener = object : Picker.OnValueChangeListener {
        override fun onValueChange(
            firstValue: String,
            secondValue: String,
            thirdValue: String,
            totalValue: String,
        ) {
            viewModel.pillCategoryInt = pillCategoryList.indexOf(firstValue)
        }
    }

    private val onCurPillCategoryChangeListener = object : Picker.OnValueChangeListener {
        override fun onValueChange(
            firstValue: String,
            secondValue: String,
            thirdValue: String,
            totalValue: String,
        ) {
            viewModel.curCategoryChange(pillCategoryList.indexOf(firstValue), firstValue)
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
            showAskDialog(
                getString(R.string.title_for_ask_want_to_finish),
                "\n",
                { finish() }
            )
        })

        viewModel.addPrescriptionEvent.observe(this@AddSelfNoOcrActivity, {
            Toast.makeText(this, "성공적으로 처방내용이 추가됐습니다", Toast.LENGTH_SHORT).show()
        })

        viewModel.failedAddPrescriptionEvent.observe(this@AddSelfNoOcrActivity, {
            Toast.makeText(this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show()
        })

        viewModel.specificTimeItemLiveData.observe(this@AddSelfNoOcrActivity, {
            specificTimeAddadapter.updateItems(it) //여기 안에 notifyDataSetChanged 있음.
        })

        viewModel.pillNameCategoryListLiveData.observe(this@AddSelfNoOcrActivity, {
            pillNameCategoryAdapter.updateItems(it)
        })

        viewModel.addErrorString.observe(this@AddSelfNoOcrActivity, {
            shortToast(viewModel.addErrorString.value?.reason ?: "모든 내용을 기입해주세요.")
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
                    setFirstRow(timeList.slice(0..22))
                    setFirstRowPosition(timeList.indexOf(viewModel.startTimeString.value)) //timeList를 슬라이스해서 넘겨진 배열은 1시~23시의 배열임. 여기서 11번째 인덱스가 12시임.
                    this.onValueChangeListener = onHopeStartTimeValueChangeListener
                }
            }
        }

        binding.periodicTime.setOnClickListener {
            bottomSheet {
                text {
                    text = "시간 주기"
                    typo = Typo.SubTitle2

                    setLayout(leftMarginPx = context.dpToIntPx(16f))
                }
                picker {
                    setFirstRow(viewModel.cycleTimeList.value!!)
                    setFirstRowPosition(timeList.indexOf(viewModel.cycleTimeString.value))
                    this.onValueChangeListener = onHopeCycleTimeValueChangeListener
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
                    setFirstRowPosition(eatPillTimeList.indexOf(viewModel.morningEatTimeString.value))
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
                    setFirstRowPosition(eatPillTimeList.indexOf(viewModel.lunchEatTimeString.value))
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
                    setFirstRowPosition(eatPillTimeList.indexOf(viewModel.dinnerEatTimeString.value))
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
                    setFirstRow(timeList)
                    setFirstRowPosition(timeList.indexOf(viewModel.specificTimeHourString.value))
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
                    setFirstRow(minutesList)
                    setFirstRowPosition(minutesList.indexOf(viewModel.specificTimeMinutesString.value))
                    this.onValueChangeListener = onSpecificMunutesTimeValueChangeListener
                }
            }
        }

        binding.specificTimeRecyclerView.apply {
            this.layoutManager = GridLayoutManager(applicationContext, 3)
            this.adapter = specificTimeAddadapter
        }
        specificTimeAddadapter.updateItems(viewModel.specificTimeItemLiveData.value!!)

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
                    setFirstRow(pillCategoryList)
                    setFirstRowPosition(pillCategoryList.indexOf(viewModel.pillCategoryString.value))
                    this.onValueChangeListener = onPillCategoryChangeListener
                }
            }
        }
        binding.specificPillRecyclerView.apply {
            this.layoutManager =
                LinearLayoutManager(this@AddSelfNoOcrActivity, LinearLayoutManager.VERTICAL, false)

            this.adapter = pillNameCategoryAdapter
        }
        pillNameCategoryAdapter.setMyItemClickListener(object :
            PillNameCategoryAdapter.MyItemClickListener {
            override fun onItemDeleteClick(position: Int) {
                viewModel.pillNameCategoryOnDeleteClick(position)
            }

            override fun onItemClick(position: Int) {

            }

            override fun onPillCategoryChangeClick(position: Int) {

                val curCategoryString =
                    viewModel.pillNameCategoryListLiveData.value!![position].pillCategory
                val curCategoryIndex = pillCategoryList.indexOf(curCategoryString)
                viewModel.changeCategoryPosition = position
                bottomSheet {
                    text {
                        text = "약 종류 재 선택"
                        typo = Typo.SubTitle2

                        setLayout(leftMarginPx = context.dpToIntPx(16f))
                    }
                    picker {
                        setFirstRow(pillCategoryList)
                        setFirstRowPosition(curCategoryIndex)
                        this.onValueChangeListener = onCurPillCategoryChangeListener
                    }
                }
            }
        })
        pillNameCategoryAdapter.updateItems(viewModel.pillNameCategoryListLiveData.value!!)

    }


    override fun onBackPressed() {
        viewModel.onClickFinish()
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

        val minutesList = listOf<String>(
            "0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"
        )


        val eatPillTimeList = listOf<String>(
            "복용 안함",
            "식전 30분",
            "식사 할때",
            "식후 30분"
        )

        val pillCategoryList = listOf<String>(
            "선택안함",
            "감기약",
            "소화기계약",
            "해열,진통,소염제",
            "영양제",
            "피부약",
            "비뇨기과 약",
            "연고류",
            "외용제",
            "안과약",
            "이과약",
            "비과약",
            "치과약",
            "신경정신과약",
            "기타 외용제",
            "시약류",
            "한약",
            "기타"
        )
    }

}

enum class PillAddErrorType(val reason: String) {
    ONE_CHOOSE_EAT_TIME("약 복용 시간을 등록해주세요"),
    ONE_REGISTER_PILL_CATEGORY("약 종류 및 목록을 한개 이상 등록해주세요"),
    REGISTER_PILL_DATE("약 복용 마감일을 선택해주세요"),
    REGISTER_ERROR("등록하는 과정에서 문제가 발생했습니다.\n잠시후에 다시 시도해주세요.")
}

enum class EatTime {
    MORNING,
    LUNCH,
    DINNER
}
