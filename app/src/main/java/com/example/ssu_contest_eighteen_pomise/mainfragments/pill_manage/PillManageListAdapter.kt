package com.example.ssu_contest_eighteen_pomise.mainfragments.pill_manage

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.SpecificTime
import com.example.ssu_contest_eighteen_pomise.databinding.PillManageSetListItemBinding
import org.joda.time.format.DateTimeFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PillManageListAdapter :
    ListAdapter<PillSetDTO, PillManageListAdapter.PillListViewHolder>(PillSetCompator()) {

    inner class PillListViewHolder(private val binding: PillManageSetListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PillSetDTO) {
            binding.item = item

            binding.itemLayout.setOnClickListener {
                myItemClickListener?.onItemClick(
                    adapterPosition
                )
            }

            binding.itemLayout.setOnLongClickListener {
                myItemClickListener?.onLongClick(adapterPosition)
                return@setOnLongClickListener true
            }
            binding.executePendingBindings()
        }
    }

    class PillSetCompator : DiffUtil.ItemCallback<PillSetDTO>() {
        override fun areItemsTheSame(oldItem: PillSetDTO, newItem: PillSetDTO): Boolean {
            return oldItem.createdAt == newItem.createdAt && oldItem.pillName == newItem.pillName &&
                    oldItem.pillCategory == newItem.pillCategory && newItem.expireDateYear == newItem.expireDateYear &&
                    oldItem.expireDateMonth == newItem.expireDateMonth && oldItem.expireDateDate == newItem.expireDateDate
        }

        override fun areContentsTheSame(oldItem: PillSetDTO, newItem: PillSetDTO): Boolean {
            return oldItem == newItem
        }
    }

    // 리사이클러뷰는 리스트 뷰와 달리 아이템에 대한 클릭 이벤트 처리를 기본제공하지 않음.
    interface MyItemClickListener {
        fun onItemClick(position: Int)
        fun onLongClick(position: Int)
    }

    private var myItemClickListener: MyItemClickListener? = null
    fun setMyItemClickListener(itemClickListener: PillManageListAdapter.MyItemClickListener) {
        myItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PillListViewHolder {
        val binding: PillManageSetListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.pill_manage_set_list_item,
            parent,
            false
        )
        return PillListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PillListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        fun timeConvert(string: String): SpecificTime {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val ldt = LocalDateTime.parse(string, dtf)
                return SpecificTime(ldt.hour, ldt.minute, ldt.second)
            } else {
                //val date = org.joda.time.LocalDateTime.now()
                val dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
                val jodatime = dtf.parseDateTime(string)
                return SpecificTime(
                    jodatime.hourOfDay,
                    jodatime.minuteOfHour,
                    jodatime.secondOfMinute
                )
            }
        }

        fun dateConvert(string: String): String {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val ldt = LocalDateTime.parse(string, dtf)
                return "${ldt.year}-${ldt.monthValue}-${ldt.dayOfMonth}"
            } else {
                //val date = org.joda.time.LocalDateTime.now()
                val dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
                val jodatime = dtf.parseDateTime(string)
                return "${jodatime.year}-${jodatime.monthOfYear}-${jodatime.dayOfMonth}"
            }
        }
    }
}

@BindingAdapter("pillName")
fun pillNameInfo(textView: TextView, item: PillSetDTO) {
    textView.text = "약 이름: ${item.pillName}"
}

@BindingAdapter("pillCategory")
fun pillCategoryInfo(textView: TextView, item: PillSetDTO) {
    textView.text = "약 종류: ${item.pillCategory}"
}

@BindingAdapter("pillSetCount")
fun pillSetInfo(textView: TextView, item: PillSetDTO) {
    textView.text = "등록된 시간 개수: ${item.id.size}"
}

@BindingAdapter("pillSetRegisteredDate")
fun pillSetRegisteredDateInfo(textView: TextView, item: PillSetDTO) {
    textView.text = "등록일: " + PillManageListAdapter.dateConvert(item.createdAt)
}

@BindingAdapter("pillSetExpireDate")
fun pillSetExpireDateInfo(textView: TextView, item: PillSetDTO) {
    textView.text = "복용마감일: ${item.expireDateYear}-${item.expireDateMonth}-${item.expireDateDate}"
}