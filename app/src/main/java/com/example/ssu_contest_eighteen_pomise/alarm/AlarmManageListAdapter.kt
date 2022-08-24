package com.example.ssu_contest_eighteen_pomise.alarm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.alarm.alarm_list_room.AlarmDTO
import com.example.ssu_contest_eighteen_pomise.databinding.*

class AlarmManageListAdapter :
    ListAdapter<AlarmDTO, RecyclerView.ViewHolder>(alarmItemCompator()) {

    inner class ReadAlarmListViewHolder(private val binding: ReadAlarmListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AlarmDTO) {
            binding.item = item

            binding.itemLayout.setOnClickListener {
                myItemClickListener?.onReadItemClick(
                    adapterPosition
                )
            }

            binding.executePendingBindings()
        }
    }

    inner class NoReadAlarmListViewHolder(private val binding: NoReadAlarmListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AlarmDTO) {
            binding.item = item
            binding.itemLayout.setOnClickListener {
                myItemClickListener?.onNoReadItemClick(
                    adapterPosition
                )
            }

            binding.executePendingBindings()
        }
    }

    class alarmItemCompator : DiffUtil.ItemCallback<AlarmDTO>() {
        override fun areItemsTheSame(oldItem: AlarmDTO, newItem: AlarmDTO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AlarmDTO, newItem: AlarmDTO): Boolean {
            return oldItem == newItem
        }
    }

    // 리사이클러뷰는 리스트 뷰와 달리 아이템에 대한 클릭 이벤트 처리를 기본제공하지 않음.
    interface MyItemClickListener {
        fun onReadItemClick(position: Int)
        fun onNoReadItemClick(position: Int)
    }

    private var myItemClickListener: MyItemClickListener? = null
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        myItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.read_alarm_list_item -> {
                val binding: ReadAlarmListItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.read_alarm_list_item,
                    parent,
                    false
                )
                val viewHolder = ReadAlarmListViewHolder(binding)
                viewHolder
            }
            else -> {
                val binding: NoReadAlarmListItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.no_read_alarm_list_item,
                    parent,
                    false
                )
                val viewHolder = NoReadAlarmListViewHolder(binding)
                viewHolder
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position].isRead) {
            R.layout.read_alarm_list_item
        } else {
            R.layout.no_read_alarm_list_item
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.read_alarm_list_item -> {
                holder as ReadAlarmListViewHolder
                holder.bind(getItem(position))
            }
            else -> {
                holder as NoReadAlarmListViewHolder
                holder.bind(getItem(position))
            }
        }
    }

    companion object {

    }
}