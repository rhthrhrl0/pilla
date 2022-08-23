package com.example.ssu_contest_eighteen_pomise.mainfragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.PillListSubPillItemBinding

class SubPillAlarmListAdapter() :
    RecyclerView.Adapter<SubPillAlarmListAdapter.SubPillAlarmListViewHolder>() {

    inner class SubPillAlarmListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = PillListSubPillItemBinding.bind(itemView)
    }

    private var mItems: List<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubPillAlarmListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.pill_list_sub_pill_item, parent, false)
        return SubPillAlarmListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubPillAlarmListViewHolder, position: Int) {
        val store: String = mItems[position] //뷰홀더와 연결시킬 Store를 얻음.
        holder.binding.textView.text=store //데이터바인딩을 위해 연결.
    }

    //외부에서 새로운 데이터를 주입해서 교체시킴.
    fun updateItems(mItems: List<String>) {
        this.mItems = mItems
        notifyDataSetChanged() //UI 갱신
    }

    override fun getItemCount()=mItems.size
}