package com.example.ssu_contest_eighteen_pomise.camera.add_by_ocr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.OcrPillSetListItemBinding

class OcrPillSetListAdapter:
    ListAdapter<AddRegisterOcrViewModel.OcrRegisterDTO, OcrPillSetListAdapter.OcrPillSetListViewHolder>(OcrPillSetCompator()) {
    //아이템 뷰 정보를 가지고 있는 클래스임.
    inner class OcrPillSetListViewHolder(private val binding: OcrPillSetListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AddRegisterOcrViewModel.OcrRegisterDTO) {
            binding.item = item
            binding.executePendingBindings()
            binding.itemLinearLayout.setOnClickListener {
                myItemClickListener?.onItemClick(binding.root,adapterPosition)
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

        override fun areContentsTheSame(oldItem: AddRegisterOcrViewModel.OcrRegisterDTO, newItem: AddRegisterOcrViewModel.OcrRegisterDTO): Boolean {
            return oldItem == newItem
        }
    }

    // 리사이클러뷰는 리스트 뷰와 달리 아이템에 대한 클릭 이벤트 처리를 기본제공하지 않음.
    interface MyItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    private var myItemClickListener: MyItemClickListener? = null
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        myItemClickListener = itemClickListener
    }

    //private var mItems: List<PatientListDTO> = ArrayList() //초반에 그냥 초기화 해놓는게 널에러 안나고 좋음.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OcrPillSetListViewHolder {
        val binding: OcrPillSetListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.ocr_pill_set_list_item,
            parent,
            false
        )
        val viewHolder = OcrPillSetListViewHolder(binding)
        binding.apply {
            binding.itemLinearLayout.setOnClickListener {
                myItemClickListener?.onItemClick(
                    binding.itemLinearLayout,
                    viewHolder.adapterPosition
                )
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: OcrPillSetListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}