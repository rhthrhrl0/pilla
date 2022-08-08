package com.example.ssu_contest_eighteen_pomise.mypage

import android.annotation.SuppressLint
import android.app.Activity
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ProtegesViewBinding
import com.example.ssu_contest_eighteen_pomise.dto.ProtegeInfo

class SettingProtegesAdapter:
    RecyclerView.Adapter<SettingProtegesAdapter.SettingProtegesViewHolder>() {
    var protegeList:ArrayList<ProtegeInfo> = ArrayList()
    private lateinit var settingItemClickListener: SettingItemClickListener
//    var pos = -1

    inner class SettingProtegesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener {
        val binding = ProtegesViewBinding.bind(itemView)
        val protegeName = binding.protegeName
        val protegeEmail = binding.protegeEmail
        val protegePhoneNum = binding.protegePhoneNum

        init { //리사이클러뷰 온클릭이벤트 설정
            binding.protege.setOnLongClickListener {
                settingItemClickListener.onItemLongClick(adapterPosition)
                return@setOnLongClickListener false
            }
//            itemView.setOnLongClickListener {
//                pos = layoutPosition
//                return@setOnLongClickListener false
//            }
            itemView.setOnCreateContextMenuListener(this)
        }

        fun bind(protegeInfo: ProtegeInfo) {
            protegeName.text = protegeInfo.username
            protegeEmail.text = protegeInfo.email
            protegePhoneNum.text = protegeInfo.phoneNumber //지금 phoneNumber 정보가 없어서 안뜨는듯
        }

        override fun onCreateContextMenu(
            p0: ContextMenu?,
            p1: View?,
            p2: ContextMenu.ContextMenuInfo?
        ) {
            (itemView.context as Activity).menuInflater.inflate(R.menu.protege_popup, p0)
        }

    }

    //클릭리스너들 관리
    interface SettingItemClickListener {
        fun onItemLongClick(position: Int)
    }
    //액티비티에서 클릭리스너 구현체를 받아서 주입
    fun setSettingItemClickListener(itemClickListener: SettingItemClickListener) {
        settingItemClickListener = itemClickListener
    }

    //리사이클러뷰 기본 호출 함수들
    //1. 사이즈
    override fun getItemCount() = protegeList.size

    //2. 뷰홀더가 생성될때 뷰를 넘겨주고 리턴
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingProtegesViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.proteges_view, parent, false)
        return SettingProtegesViewHolder(inflatedView)
    }

    //3. 스크롤 등으로 화면 이동시 레이아웃 재구성
    override fun onBindViewHolder(holder: SettingProtegesViewHolder, position: Int) {
        holder.bind(protegeList[position])
//        val store:ProtectorDTO = protectorList[position]
//        holder.binding.protectors = store
    }

    fun updateItems(items: ArrayList<ProtegeInfo>) {
        protegeList = items
        this.notifyDataSetChanged()
    }
}