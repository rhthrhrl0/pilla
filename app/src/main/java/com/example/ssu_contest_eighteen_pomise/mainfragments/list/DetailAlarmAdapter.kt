package com.example.ssu_contest_eighteen_pomise.mainfragments.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.PillAlarmDetailBinding
import com.example.ssu_contest_eighteen_pomise.dto.PillAlarmDetail

class DetailAlarmAdapter:RecyclerView.Adapter<DetailAlarmAdapter.DetailAlaramViewHolder>() {
    var detailAlarmList:ArrayList<PillAlarmDetail> = ArrayList()

    inner class DetailAlaramViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val binding = PillAlarmDetailBinding.bind(itemView)
        val detailName = binding.detailName
        val detailEntp = binding.detailEntp
        val detailChart = binding.detailChart
        val detailEe = binding.detailEe

        init {

        }

        fun bind(pillAlarmDetail: PillAlarmDetail) {
            detailName.text = pillAlarmDetail.name
            detailEntp.text = pillAlarmDetail.entp
            detailChart.text = pillAlarmDetail.chart
            detailEe.text = pillAlarmDetail.ee
        }
    }
    //리사이클러뷰 기본 호출 함수들
    //1. 사이즈
    override fun getItemCount() = detailAlarmList.size

    //2. 뷰홀더가 생성될때 뷰를 넘겨주고 리턴
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailAlaramViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.pill_alarm_detail, parent, false)
        return DetailAlaramViewHolder(inflatedView)
    }

    //3. 스크롤 등으로 화면 이동시 레이아웃 재구성
    override fun onBindViewHolder(holder: DetailAlaramViewHolder, position: Int) {
        holder.bind(detailAlarmList[position])
    }

    fun updateItems(items:ArrayList<PillAlarmDetail>) {
        Log.d("kyb", detailAlarmList.size.toString())
        detailAlarmList = items
        this.notifyDataSetChanged()
    }

}