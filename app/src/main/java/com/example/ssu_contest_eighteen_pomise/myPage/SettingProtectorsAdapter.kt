package com.example.ssu_contest_eighteen_pomise.myPage

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ssu_contest_eighteen_pomise.dto.UserDTO

class SettingProtectorsAdapter(private val users: ArrayList<UserDTO>)
    : RecyclerView.Adapter<SettingProtectorsAdapter.ViewHolder>() {
    lateinit var list:List<UserDTO>

    override fun getItemCount(): Int = users.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    fun updateItem(list:List<UserDTO>) {
        this.list = list
        notifyDataSetChanged()
    }

    // 각 항목에 필요한 기능을 구현
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        fun bind(listener: View.OnClickListener, item: UserDTO) {
            view.setOnClickListener(listener)
        }
    }
}