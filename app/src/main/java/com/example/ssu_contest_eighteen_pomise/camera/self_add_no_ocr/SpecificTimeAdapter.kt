package com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.platform.compositionContext
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.AddElementItemStoreBinding


class SpecificTimeAddAdapter() :
    RecyclerView.Adapter<SpecificTimeAddAdapter.SpecificTimeAddViewHolder>() {
    //아이템 뷰 정보를 가지고 있는 클래스임.
    inner class SpecificTimeAddViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = AddElementItemStoreBinding.bind(itemView)

        init {
            binding.deleteIt.setOnClickListener {
                myItemClickListener.onItemClick(adapterPosition)
            }
        }
    }

    private var mItems: List<SpecificTime> = ArrayList<SpecificTime>() //초반에 그냥 초기화 해놓는게 널에러 안나고 좋음.

    // 리사이클러뷰는 리스트 뷰와 달리 아이템에 대한 클릭 이벤트 처리를 기본제공하지 않음.
    interface MyItemClickListener {
        fun onItemClick(position: Int)
        fun onItemLongClick(position: Int)
    }

    private lateinit var myItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        myItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecificTimeAddViewHolder {
        //안드로이드의 모든 뷰 및 뷰그룹에서 자체적으로 컨텍스트를 얻을 수 있다. 이 리사이클러뷰가 붙을 뷰그룹의 컨텍스트를 얻음.
        // 이건 그냥 외우면 됨. 지정한 레이아웃 대로 인플레이트 시킬건데, parent 에게 올릴 뷰임. 근데 인플레이트 시킬때 붙여서 나오지는 않게
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.add_element_item_store, parent, false)

        // 인플레이트 된 view를 지닌 뷰홀더를 생성해서 리턴함.
        return SpecificTimeAddViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpecificTimeAddViewHolder, position: Int) {
        val store: SpecificTime = mItems[position] //뷰홀더와 연결시킬 Store를 얻음.
        holder.binding.specificTime = store //데이터바인딩을 위해 연결.
    }

    override fun getItemCount(): Int = mItems.size

    //외부에서 새로운 데이터를 주입해서 교체시킴.
    fun updateItems(mItems: List<SpecificTime>) {
        this.mItems = mItems
        notifyDataSetChanged() //UI 갱신
    }

}

@BindingAdapter("specificTimeText")
fun setSpecificTimeText(textView: TextView, store: SpecificTime) {
    var specificTime = "${store.hour}시 ${store.minutes}분"
    textView.text = specificTime
}