package com.example.ssu_contest_eighteen_pomise.mainfragments.patient_list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.PatientListItemBinding
import com.example.ssu_contest_eighteen_pomise.databinding.PillListItemBinding
import com.example.ssu_contest_eighteen_pomise.mainfragments.list.AlarmListDTO

class PatientListAdapter :
    ListAdapter<PatientListDTO, PatientListAdapter.PatientListViewHolder>(PatientCompator()) {
    //아이템 뷰 정보를 가지고 있는 클래스임.
    inner class PatientListViewHolder(private val binding: PatientListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PatientListDTO) {
            binding.patient = item
            binding.executePendingBindings()
        }
    }

    class PatientCompator:DiffUtil.ItemCallback<PatientListDTO>(){
        override fun areItemsTheSame(oldItem: PatientListDTO, newItem: PatientListDTO): Boolean {
            return oldItem.email == newItem.email
        }

        override fun areContentsTheSame(oldItem: PatientListDTO, newItem: PatientListDTO): Boolean {
            return oldItem == newItem
        }
    }

    // 리사이클러뷰는 리스트 뷰와 달리 아이템에 대한 클릭 이벤트 처리를 기본제공하지 않음.
    interface MyItemClickListener {
        fun onItemClick(position: Int, email: String)
    }

    private var myItemClickListener: MyItemClickListener? = null
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        myItemClickListener = itemClickListener
    }

    //private var mItems: List<PatientListDTO> = ArrayList() //초반에 그냥 초기화 해놓는게 널에러 안나고 좋음.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientListViewHolder {
        val binding: PatientListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.patient_list_item,
            parent,
            false
        )
        val viewHolder = PatientListViewHolder(binding)
        binding.apply {
            binding.itemLinearLayout.setOnClickListener {
                myItemClickListener?.onItemClick(
                    viewHolder.adapterPosition,
                    currentList.elementAt(viewHolder.adapterPosition).email
                )
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: PatientListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}