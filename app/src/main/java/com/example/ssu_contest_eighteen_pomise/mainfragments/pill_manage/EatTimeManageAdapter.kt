package com.example.ssu_contest_eighteen_pomise.mainfragments.pill_manage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.PillEatTimeListItemBinding

class EatTimeManageAdapter :ListAdapter<PillManageDetailViewModel.PillTime,EatTimeManageAdapter.EatTimeListViewHolder>(EatTimeCompator()) {
    inner class EatTimeListViewHolder(private val binding: PillEatTimeListItemBinding ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PillManageDetailViewModel.PillTime) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    class EatTimeCompator : DiffUtil.ItemCallback<PillManageDetailViewModel.PillTime>() {
        override fun areItemsTheSame(oldItem: PillManageDetailViewModel.PillTime, newItem: PillManageDetailViewModel.PillTime): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: PillManageDetailViewModel.PillTime, newItem: PillManageDetailViewModel.PillTime): Boolean {
            return oldItem == newItem
        }
    }

    // 리사이클러뷰는 리스트 뷰와 달리 아이템에 대한 클릭 이벤트 처리를 기본제공하지 않음.
    interface MyItemClickListener {
        fun onItemDeleteClick(position: Int)
    }

    private var myItemClickListener: MyItemClickListener? = null
    fun setMyItemClickListener(itemClickListener: EatTimeManageAdapter.MyItemClickListener) {
        myItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EatTimeListViewHolder {
        val binding: PillEatTimeListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.pill_eat_time_list_item,
            parent,
            false
        )
        val viewHolder = EatTimeListViewHolder(binding)
        binding.apply {
            binding.deleteBtn.setOnClickListener {
                myItemClickListener?.onItemDeleteClick(
                    viewHolder.adapterPosition
                )
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: EatTimeListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

