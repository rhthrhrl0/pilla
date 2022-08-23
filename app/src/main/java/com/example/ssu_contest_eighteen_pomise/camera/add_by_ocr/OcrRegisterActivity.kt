package com.example.ssu_contest_eighteen_pomise.camera.add_by_ocr

import android.app.AlertDialog
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.AddSelfNoOcrActivity
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.PillNameCategoryAdapter
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.SpecificTimeAddAdapter
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityOcrRegisterBinding
import com.example.ssu_contest_eighteen_pomise.databinding.OcrNonSelectedSetListItemBinding
import com.example.ssu_contest_eighteen_pomise.databinding.OcrSelectedSetListItemBinding
import com.example.ssu_contest_eighteen_pomise.dto.OcrAndImageData
import com.yourssu.design.system.atom.Picker
import com.yourssu.design.system.foundation.Typo
import com.yourssu.design.system.language.bottomSheet
import com.yourssu.design.system.language.picker
import com.yourssu.design.system.language.setLayout
import com.yourssu.design.system.language.text
import com.yourssu.design.undercarriage.size.dpToIntPx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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

    private val onCurPillCategoryChangeListener = object : Picker.OnValueChangeListener {
        override fun onValueChange(
            firstValue: String,
            secondValue: String,
            thirdValue: String,
            totalValue: String,
        ) {
            viewModel.curCategoryChange(
                AddSelfNoOcrActivity.pillCategoryList.indexOf(firstValue),
                firstValue
            )
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
        viewModel.pillSetList.observe(this, {
            if (it.size == 1) {
                binding.scrollView.visibility=View.GONE
                binding.emptyText.visibility=View.VISIBLE
            } else if (it.size > 1) {
                binding.scrollView.visibility=View.VISIBLE
                binding.emptyText.visibility=View.GONE
            }
            ocrPillSetAdapter.submitList(it)
        })

        viewModel.specificTimeItemLiveData.observe(this, {
            specificTimeAddadapter.updateItems(it) //여기 안에 notifyDataSetChanged 있음.
        })

        viewModel.pillNameCategoryListLiveData.observe(this, {
            pillNameCategoryAdapter.updateItems(it)
        })

        repeatOnStart {
            viewModel.eventFlow.collect {
                handleEvent(it)
            }
        }
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
                    setFirstRow(AddSelfNoOcrActivity.timeList.slice(0..22))
                    setFirstRowPosition(AddSelfNoOcrActivity.timeList.indexOf(viewModel.startTimeString.value))
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
                    setFirstRowPosition(AddSelfNoOcrActivity.timeList.indexOf(viewModel.cycleTimeString.value))
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
                    setFirstRow(AddSelfNoOcrActivity.eatPillTimeList)
                    setFirstRowPosition(AddSelfNoOcrActivity.eatPillTimeList.indexOf(viewModel.morningEatTimeString.value))
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
                    setFirstRowPosition(AddSelfNoOcrActivity.eatPillTimeList.indexOf(viewModel.lunchEatTimeString.value))
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
                    setFirstRowPosition(AddSelfNoOcrActivity.eatPillTimeList.indexOf(viewModel.dinnerEatTimeString.value))
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
                    setFirstRowPosition(AddSelfNoOcrActivity.timeList.indexOf(viewModel.specificTimeHourString.value))
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
                    setFirstRowPosition(AddSelfNoOcrActivity.minutesList.indexOf(viewModel.specificTimeMinutesString.value))
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
                    setFirstRowPosition(AddSelfNoOcrActivity.pillCategoryList.indexOf(viewModel.pillCategoryString.value))
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
                override fun onSelectedItemClick(
                    binding: OcrSelectedSetListItemBinding,
                    position: Int
                ) {
                    // 딱히 여기선 할거 없음.
                    Log.d("kmj", "이미 선택된 아이템 선택.${position}")
                }

                override fun onDeleteItemSelectedClick(position: Int) {
                    showAskDialog(
                        "정말 해당 약 목록을 삭제하시겠습니까?",
                        "\n"
                    ) {
                        viewModel.onDeleteItemSelectedClick(position)
                    }
                }

                override fun onNonSelectedItemClick(
                    binding: OcrNonSelectedSetListItemBinding,
                    position: Int
                ) {
                    viewModel.onNonSelectedItemClick(position)
                }

                override fun onDeleteItemNonSelectedClick(position: Int) {
                    showAskDialog(
                        "정말 해당 약 목록을 삭제하시겠습니까?",
                        "\n"
                    ) {
                        viewModel.onDeleteItemNonSelectedClick(position)
                    }
                }

                override fun onPlusItemClick(position: Int) {
                    viewModel.onPlusItemClick(position)
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

            override fun onPillCategoryChangeClick(position: Int) {
                if (viewModel.curIndex != -1) {
                    val curCategoryString =
                        viewModel.pillSetList.value!![viewModel.curIndex].pillNameCategoryList[position].pillCategory
                    val curCategoryIndex =
                        AddSelfNoOcrActivity.pillCategoryList.indexOf(curCategoryString)
                    viewModel.changeCategoryPosition = position
                    bottomSheet {
                        text {
                            text = "약 종류 재 선택"
                            typo = Typo.SubTitle2

                            setLayout(leftMarginPx = context.dpToIntPx(16f))
                        }
                        picker {
                            setFirstRow(AddSelfNoOcrActivity.pillCategoryList)
                            setFirstRowPosition(curCategoryIndex)
                            this.onValueChangeListener = onCurPillCategoryChangeListener
                        }
                    }
                }
            }
        })
    }

    fun repeatOnStart(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    fun handleEvent(event: AddRegisterOcrViewModel.MyEvent) {
        when (event) {
            is AddRegisterOcrViewModel.MyEvent.FinishEvent -> {
                showAskDialog(
                    getString(R.string.title_for_ask_want_to_finish),
                    getString(R.string.message_for_warning_about_ocr),
                    { finish() }
                )
            }
            is AddRegisterOcrViewModel.MyEvent.AddSuccessEvent -> {
                Toast.makeText(this, "성공적으로 처방내용이 추가됐습니다", Toast.LENGTH_SHORT).show()
                finish()
            }
            is AddRegisterOcrViewModel.MyEvent.AddFailedNoListEvent -> {
                Toast.makeText(
                    this,
                    "등록할 내용이 없습니다.",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.addNow = false
            }
            is AddRegisterOcrViewModel.MyEvent.AddFailedEvent -> {
                Toast.makeText(
                    this,
                    "${event.position + 1}번째의 ${event.pillEatMethod}에서 ${event.failedReason}",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.addNow = false
            }
            is AddRegisterOcrViewModel.MyEvent.AddFailedFromServer -> {
                Toast.makeText(
                    this,
                    "서버로부터 등록하는데 실패했습니다.\n 잠시 후 다시 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.addNow = false
            }
        }
    }

    private fun showAskDialog(title: String, message: String, onFunc: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(R.string.close) { _, _ -> }
            .setPositiveButton(R.string.confirm) { _, _ ->
                onFunc()
            }.setCancelable(false)
            .show()
    }

    override fun onBackPressed() {
        viewModel.sendEvent(AddRegisterOcrViewModel.MyEvent.FinishEvent)
    }

    companion object {
        const val KEY_OCR_DATA_NAME = "key_ocr_data_name_"
    }
}