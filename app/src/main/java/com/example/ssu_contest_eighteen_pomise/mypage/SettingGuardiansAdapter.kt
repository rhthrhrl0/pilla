package com.example.ssu_contest_eighteen_pomise.mypage

import android.annotation.SuppressLint
import android.app.Activity
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.GuardiansViewBinding
import com.example.ssu_contest_eighteen_pomise.dto.GuardianDTO
import com.example.ssu_contest_eighteen_pomise.dto.GuardianInfo

class SettingGuardiansAdapter:
    RecyclerView.Adapter<SettingGuardiansAdapter.SettingGuardiansViewHolder>() {
    var guardianList:ArrayList<GuardianInfo> = ArrayList()
    private lateinit var settingItemClickListener: SettingItemClickListener
    var pos = -1

    inner class SettingGuardiansViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener {
        val binding = GuardiansViewBinding.bind(itemView)
        val guardianName = binding.guardianName
        val guardianEmail = binding.guardianEmail
        val guardianPhoneNum = binding.guardianPhoneNum

        init { //리사이클러뷰 온클릭이벤트 설정
            binding.guardian.setOnLongClickListener {
                settingItemClickListener.onItemLongClick(adapterPosition)
                return@setOnLongClickListener false
            }
//            itemView.setOnLongClickListener {
//                pos = layoutPosition
//                return@setOnLongClickListener false
//            }
            itemView.setOnCreateContextMenuListener(this)
        }

        fun bind(guardianInfo: GuardianInfo) {
            guardianName.text = guardianInfo.username
            guardianEmail.text = guardianInfo.email
            guardianPhoneNum.text = guardianInfo.phoneNumber //지금 phoneNumber 정보가 없어서 안뜨는듯
        }

        override fun onCreateContextMenu(
            p0: ContextMenu?,
            p1: View?,
            p2: ContextMenu.ContextMenuInfo?
        ) {
            (itemView.context as Activity).menuInflater.inflate(R.menu.guardian_popup, p0)
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
    override fun getItemCount() = guardianList.size

    //2. 뷰홀더가 생성될때 뷰를 넘겨주고 리턴
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingGuardiansViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.guardians_view, parent, false)
        return SettingGuardiansViewHolder(inflatedView)
    }

    //3. 스크롤 등으로 화면 이동시 레이아웃 재구성
    override fun onBindViewHolder(holder: SettingGuardiansViewHolder, position: Int) {
        holder.bind(guardianList[position])
//        val store:ProtectorDTO = protectorList[position]
//        holder.binding.protectors = store
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(items: ArrayList<GuardianInfo>) {
        guardianList = items
        this.notifyDataSetChanged()
    }
}