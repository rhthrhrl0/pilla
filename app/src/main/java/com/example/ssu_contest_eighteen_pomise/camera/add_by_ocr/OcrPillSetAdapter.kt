package com.example.ssu_contest_eighteen_pomise.camera.add_by_ocr

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.OcrNonSelectedSetListItemBinding
import com.example.ssu_contest_eighteen_pomise.databinding.OcrPlusObjectItemBinding
import com.example.ssu_contest_eighteen_pomise.databinding.OcrSelectedSetListItemBinding

class OcrPillSetListAdapter :
    ListAdapter<AddRegisterOcrViewModel.OcrRegisterDTO, RecyclerView.ViewHolder>(
        OcrPillSetCompator()
    ) {
    //아이템 뷰 정보를 가지고 있는 클래스임.
    inner class OcrPillSetSelectedListViewHolder(private val binding: OcrSelectedSetListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AddRegisterOcrViewModel.OcrRegisterDTO) {
            binding.item = item
            binding.executePendingBindings()
            binding.itemLinearLayout.setOnClickListener {
                myItemClickListener?.onSelectedItemClick(binding, adapterPosition)
            }
            binding.deleteIt.setOnClickListener {
                myItemClickListener?.onDeleteItemSelectedClick(adapterPosition)
            }
            binding.textView.setHorizontallyScrolling(true)
            binding.textView.isSelected = true
        }
    }

    inner class OcrPillSetNonSelectedListViewHolder(private val binding: OcrNonSelectedSetListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AddRegisterOcrViewModel.OcrRegisterDTO) {
            binding.item = item
            binding.executePendingBindings()
            binding.itemLinearLayout.setOnClickListener {
                myItemClickListener?.onNonSelectedItemClick(binding, adapterPosition)
            }
            binding.deleteIt.setOnClickListener {
                myItemClickListener?.onDeleteItemNonSelectedClick(adapterPosition)
            }
        }
    }

    inner class OcrPlusObjectViewHolder(private val binding: OcrPlusObjectItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item: AddRegisterOcrViewModel.OcrRegisterDTO) {
            binding.item = item
            binding.executePendingBindings()
            binding.itemLinearLayout.setOnClickListener {
                myItemClickListener?.onPlusItemClick(adapterPosition)
            }
        }
    }


    class OcrPillSetCompator : DiffUtil.ItemCallback<AddRegisterOcrViewModel.OcrRegisterDTO>() {
        override fun areItemsTheSame(
            oldItem: AddRegisterOcrViewModel.OcrRegisterDTO,
            newItem: AddRegisterOcrViewModel.OcrRegisterDTO
        ): Boolean {
            return oldItem.pillEatCount == newItem.pillEatCount && oldItem.pillEatDay == newItem.pillEatDay && oldItem.pillEatMethod == newItem.pillEatMethod
        }

        override fun areContentsTheSame(
            oldItem: AddRegisterOcrViewModel.OcrRegisterDTO,
            newItem: AddRegisterOcrViewModel.OcrRegisterDTO
        ): Boolean {
            return oldItem == newItem
        }
    }

    // 리사이클러뷰는 리스트 뷰와 달리 아이템에 대한 클릭 이벤트 처리를 기본제공하지 않음.
    interface MyItemClickListener {
        fun onSelectedItemClick(binding: OcrSelectedSetListItemBinding, position: Int)
        fun onDeleteItemSelectedClick(position: Int)
        fun onNonSelectedItemClick(binding: OcrNonSelectedSetListItemBinding, position: Int)
        fun onDeleteItemNonSelectedClick(position: Int)
        fun onPlusItemClick(position: Int)
    }

    private var myItemClickListener: MyItemClickListener? = null
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        myItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            AddRegisterOcrViewModel.SELECTED_SET_ITEM->{
                val binding: OcrSelectedSetListItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.ocr_selected_set_list_item,
                    parent,
                    false
                )
                val viewHolder = OcrPillSetSelectedListViewHolder(binding)
                viewHolder
            }
            AddRegisterOcrViewModel.NON_SELECTED_SET_ITEM->{
                val binding: OcrNonSelectedSetListItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.ocr_non_selected_set_list_item,
                    parent,
                    false
                )
                val viewHolder = OcrPillSetNonSelectedListViewHolder(binding)
                viewHolder
            }
            else ->{
                val binding: OcrPlusObjectItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.ocr_plus_object_item,
                    parent,
                    false
                )
                val viewHolder = OcrPlusObjectViewHolder(binding)
                viewHolder
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].typeId
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItem(position).typeId){
            AddRegisterOcrViewModel.SELECTED_SET_ITEM->{
                holder as OcrPillSetSelectedListViewHolder
                holder.bind(getItem(position))
            }
            AddRegisterOcrViewModel.NON_SELECTED_SET_ITEM->{
                holder as OcrPillSetNonSelectedListViewHolder
                holder.bind(getItem(position))
            }
            else ->{
                holder as OcrPlusObjectViewHolder
                holder.bind(getItem(position))
            }
        }
    }
}