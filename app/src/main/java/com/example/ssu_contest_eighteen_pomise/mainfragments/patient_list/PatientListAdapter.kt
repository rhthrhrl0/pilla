package com.example.ssu_contest_eighteen_pomise.mainfragments.patient_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.PatientNonSelectedItemBinding
import com.example.ssu_contest_eighteen_pomise.databinding.PatientSelectedItemBinding

class PatientListAdapter :
    ListAdapter<PatientListDTO, RecyclerView.ViewHolder>(PatientCompator()) {
    //아이템 뷰 정보를 가지고 있는 클래스임.
    inner class PatientSelectedViewHolder(private val binding: PatientSelectedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PatientListDTO) {
            binding.patient = item
            binding.itemLinearLayout.setOnClickListener {
                binding.textView.setHorizontallyScrolling(true)
                binding.textView.isSelected=true
                myItemClickListener?.onSelectedItemClick(adapterPosition, item.email)
            }
            binding.executePendingBindings()
        }
    }

    inner class PatientNonSelectedViewHolder(private val binding: PatientNonSelectedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PatientListDTO) {
            binding.patient = item
            binding.itemLinearLayout.setOnClickListener {
                myItemClickListener?.onNonSelectedItemClick(adapterPosition, item.email)
            }
            binding.executePendingBindings()
        }
    }

    class PatientCompator : DiffUtil.ItemCallback<PatientListDTO>() {
        override fun areItemsTheSame(oldItem: PatientListDTO, newItem: PatientListDTO): Boolean {
            return oldItem.email == newItem.email
        }

        override fun areContentsTheSame(oldItem: PatientListDTO, newItem: PatientListDTO): Boolean {
            return oldItem == newItem
        }
    }

    // 리사이클러뷰는 리스트 뷰와 달리 아이템에 대한 클릭 이벤트 처리를 기본제공하지 않음.
    interface MyItemClickListener {
        fun onSelectedItemClick(position: Int, email: String)
        fun onNonSelectedItemClick(position: Int, email: String)
    }

    private var myItemClickListener: MyItemClickListener? = null
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        myItemClickListener = itemClickListener
    }

    //private var mItems: List<PatientListDTO> = ArrayList() //초반에 그냥 초기화 해놓는게 널에러 안나고 좋음.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            R.layout.patient_selected_item -> {
                val binding: PatientSelectedItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.patient_selected_item,
                    parent,
                    false
                )
                val viewHolder = PatientSelectedViewHolder(binding)
                viewHolder
            }
            else -> {
                val binding: PatientNonSelectedItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.patient_non_selected_item,
                    parent,
                    false
                )
                val viewHolder = PatientNonSelectedViewHolder(binding)
                viewHolder
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position].isSelected) {
            R.layout.patient_selected_item
        } else {
            R.layout.patient_non_selected_item
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.patient_selected_item -> {
                holder as PatientSelectedViewHolder
                holder.bind(getItem(position))
            }
            R.layout.patient_non_selected_item -> {
                holder as PatientNonSelectedViewHolder
                holder.bind(getItem(position))
            }
        }
    }

}