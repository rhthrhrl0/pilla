package com.example.ssu_contest_eighteen_pomise.mainfragments.list

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.SpecificTime
import com.example.ssu_contest_eighteen_pomise.databinding.PillListItemBinding

class PillListAdapter() : RecyclerView.Adapter<PillListAdapter.PillListViewHolder>() {
    //아이템 뷰 정보를 가지고 있는 클래스임.
    inner class PillListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = PillListItemBinding.bind(itemView)

        init {
            binding.itemLinearLayout.setOnClickListener {
                myItemClickListener?.onItemClick(adapterPosition)
            }
            binding.morePillInfo.setOnClickListener {
                myItemClickListener?.onMorePillInfoClick(adapterPosition,binding.morePillInfo)
            }
        }
    }

    // 리사이클러뷰는 리스트 뷰와 달리 아이템에 대한 클릭 이벤트 처리를 기본제공하지 않음.
    interface MyItemClickListener{
        fun onItemClick(position: Int)
        fun onMorePillInfoClick(position: Int,view:View)
        fun onItemLongClick(position: Int)
    }

    private var myItemClickListener: MyItemClickListener?=null
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        myItemClickListener=itemClickListener
    }

    private var mItems: List<AlarmListDTO> = ArrayList() //초반에 그냥 초기화 해놓는게 널에러 안나고 좋음.

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PillListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.pill_list_item, parent, false)
        return PillListViewHolder(view)
    }

    override fun onBindViewHolder(holder: PillListViewHolder, position: Int) {
        val store: AlarmListDTO = mItems[position] //뷰홀더와 연결시킬 Store를 얻음.
        holder.binding.pillDTO = store //데이터바인딩을 위해 연결.
    }

    //외부에서 새로운 데이터를 주입해서 교체시킴.
    fun updateItems(mItems: List<AlarmListDTO>) {
        this.mItems = mItems
        notifyDataSetChanged() //UI 갱신
    }

    override fun getItemCount() = mItems.size
}

@BindingAdapter("pillIsNextEat")
fun setPillIsNextEat(textView: TextView, store: AlarmListDTO) {
    var pillIsNextEatString = ""
    if (store.isNextEatPill) {
        pillIsNextEatString = "다음 복용 알림"
    } else {
        pillIsNextEatString = "예정된 알림"
    }
    textView.text = pillIsNextEatString
}

@BindingAdapter("pillEatTimeText")
fun setPillEatTimeText(textView: TextView, store: AlarmListDTO) {
    var pillEatTime = "${store.eatHour}시 ${store.eatMinutes}분"
    textView.text = pillEatTime
}

@BindingAdapter("pillNameOneVisible")
fun setPillNameOneVisible(textView: TextView, store: AlarmListDTO) {
    if (store.pillList.isNotEmpty()) {
        textView.visibility = View.VISIBLE
        textView.text = store.pillList.elementAt(0).pillCategory
    } else {
        textView.visibility = View.GONE
    }
}

@BindingAdapter("pillNameTwoVisible")
fun setPillNameTwoVisible(textView: TextView, store: AlarmListDTO) {
    if (store.pillList.isNotEmpty() && store.pillList.size >= 2) {
        textView.visibility = View.VISIBLE
        textView.text = store.pillList.elementAt(1).pillCategory
    } else {
        textView.visibility = View.GONE
    }
}

@BindingAdapter("pillNameThreeVisible")
fun setPillNameThreeVisible(textView: TextView, store: AlarmListDTO) {
    if (store.pillList.isNotEmpty() && store.pillList.size >= 3) {
        textView.visibility = View.VISIBLE
        if (store.pillList.size > 3) {
            textView.text = "그 외"
        } else {
            textView.text = store.pillList.elementAt(2).pillCategory
        }
    } else {
        textView.visibility = View.GONE
    }
}
