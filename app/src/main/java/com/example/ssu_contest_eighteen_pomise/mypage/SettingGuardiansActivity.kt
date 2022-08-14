package com.example.ssu_contest_eighteen_pomise.mypage

import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivitySettingGuardiansBinding

class SettingGuardiansActivity:AppCompatActivity() {
    private lateinit var binding: ActivitySettingGuardiansBinding
    private val viewModel: SettingGuardiansViewModel by viewModels()
    private var adapter = SettingGuardiansAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting_guardians)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        onViewModelInit()
        initView()
        viewModel.retrofit()
    }

//    //context menu 관리
//    override fun onCreateContextMenu(
//        menu: ContextMenu?,
//        v: View?,
//        menuInfo: ContextMenu.ContextMenuInfo?
//    ) {
//        super.onCreateContextMenu(menu, v, menuInfo)
//        menuInflater.inflate(R.menu.guardian_popup, menu)
//    }
//
//    //아이템이 선택되었을 때 할 작업을 정의
//    override fun onContextItemSelected(item: MenuItem): Boolean {
//
//        when(item.itemId) {
//            R.id.menu_edit_guardian -> {
//                Log.d("kyb", "수정버튼 클릭")
//                Toast.makeText(this, "수정버튼 클릭", Toast.LENGTH_SHORT).show()
//            }
//            R.id.menu_delete_guardian -> {
//                Log.d("kyb", "삭제버튼 클릭")
//                Toast.makeText(this, "삭제버튼 클릭", Toast.LENGTH_SHORT).show()
//            }
//        }
//        return true
//    }

    private fun onViewModelInit() {
        viewModel.btn_finish.observe(this, {
            backToPrevPage()
        })

        viewModel.guardianList.observe(this, {
            adapter.updateItems(it)
        })
    }

    private fun initView() {
        binding.guardianRecyclerView.apply {
            this.layoutManager = LinearLayoutManager(this@SettingGuardiansActivity)
            this.adapter = this@SettingGuardiansActivity.adapter
        }

//        adapter.setSettingItemClickListener(object :
//            SettingGuardiansAdapter.SettingItemClickListener {
//
//            override fun onItemLongClick(position: Int) { //수정,삭제 팝업 띄우기
//                Log.d("kyb", "position : "+position.toString())
//                Log.d("kyb", "onItemLongClick works")
//            }
//        })

//        adapter.updateItems(viewModel.protectorsList.value!!)
    }

    private fun backToPrevPage() {
        finish()
    }
}