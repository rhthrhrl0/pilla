package com.example.ssu_contest_eighteen_pomise.mainfragments.patient_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.PatientListItemBinding
import com.example.ssu_contest_eighteen_pomise.databinding.PillListItemBinding
import com.example.ssu_contest_eighteen_pomise.mainfragments.list.AlarmListDTO

class PatientListAdapter : RecyclerView.Adapter<PatientListAdapter.PatientListViewHolder>() {
    //아이템 뷰 정보를 가지고 있는 클래스임.
    inner class PatientListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = PatientListItemBinding.bind(itemView)

        init {
            binding.itemLinearLayout.setOnClickListener {
                myItemClickListener?.onItemClick(
                    adapterPosition,
                    mItems.elementAt(adapterPosition).email
                )
            }
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

    private var mItems: List<PatientListDTO> = ArrayList() //초반에 그냥 초기화 해놓는게 널에러 안나고 좋음.

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.patient_list_item, parent, false)
        return PatientListViewHolder(view)
    }

    override fun onBindViewHolder(holder: PatientListViewHolder, position: Int) {
        val store: PatientListDTO = mItems[position] //뷰홀더와 연결시킬 Store를 얻음.
        holder.binding.patient = store //데이터바인딩을 위해 연결.
    }

    //외부에서 새로운 데이터를 주입해서 교체시킴.
    fun updateItems(mItems: List<PatientListDTO>) {
        this.mItems = mItems
        notifyDataSetChanged() //UI 갱신
    }

    override fun getItemCount() = mItems.size
}