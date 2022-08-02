package com.example.ssu_contest_eighteen_pomise.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ProtectorsViewBinding
import com.example.ssu_contest_eighteen_pomise.dto.ProtectorDTO

class SettingProtectorsAdapter:RecyclerView.Adapter<SettingProtectorsAdapter.SettingProtectorsViewHolder>() {

    inner class SettingProtectorsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val binding = ProtectorsViewBinding.bind(itemView)
        init {

        }
    }

    private var protectors:List<ProtectorDTO> = ArrayList()


    interface MyItemClickListener {
        fun onItemClick(position: Int)
    }
    private lateinit var myItemClickListener: MyItemClickListener

    override fun getItemCount()=protectors.size

    //뷰홀더가 생성될때 뷰를 넘겨주고 리턴
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingProtectorsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.protectors_view, parent, false)
        return SettingProtectorsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SettingProtectorsViewHolder, position: Int) {
        val store: ProtectorDTO = protectors[position]
        holder.binding.protectors = store
    }

    fun updateItems(items:List<ProtectorDTO>) {
        this.protectors = items
        notifyDataSetChanged()
    }

    // 각 항목에 필요한 기능을 구현
//    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
//        private var view: View = v
//        fun bind(listener: View.OnClickListener, item: ProtectorDTO) {
//            view.setOnClickListener(listener)
//        }
//    }
}