package com.example.ssu_contest_eighteen_pomise.mypage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityAddProtegeBinding
import com.example.ssu_contest_eighteen_pomise.databinding.ActivitySettingProtegesBinding

class SettingProtegesActivity:AppCompatActivity() {
    private lateinit var binding: ActivitySettingProtegesBinding
    private val viewModel: SettingProtegesViewModel by viewModels()
    private var adapter = SettingProtegesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting_proteges)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        onViewModelInit()
        initView()
        viewModel.retrofit()
    }

    //context menu 관리
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.protege_popup, menu)
    }

    //menu 아이템이 선택되었을 때 할 작업을 정의
    //최종적으로 선택한 리사이클러뷰의 email을 레트로핏으로 전송해주면 됨
    override fun onContextItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.menu_delete_protege -> {
                Log.d("kyb", findViewById<TextView>(R.id.protege_email).toString())
            }
        }
        return true
    }

    private fun onViewModelInit() {
        viewModel.btn_finish.observe(this, {
            backToPrevPage()
        })

        viewModel.protegeList.observe(this, {
            adapter.updateItems(it)
        })

        viewModel.addProtege.observe(this, {
            loadAddPage()
        })

        viewModel.succeedAddProtege.observe(this, {
            Toast.makeText(this, "환자가 추가되었습니다", Toast.LENGTH_SHORT).show()
        })

        viewModel.failedAddProtege.observe(this, {
            Toast.makeText(this, "추가에 실패했습니다", Toast.LENGTH_SHORT).show()
        })
    }


    private fun initView() {
        binding.protegeRecyclerView.apply {
            this.layoutManager = LinearLayoutManager(this@SettingProtegesActivity)
            this.adapter = this@SettingProtegesActivity.adapter
        }

        adapter.setSettingItemClickListener(object :
            SettingProtegesAdapter.SettingItemClickListener {

            override fun onItemLongClick(position: Int) { //수정,삭제 팝업 띄우기
                Log.d("kyb", "onItemLongClick position : "+position.toString())
            }

        })

//        adapter.updateItems(viewModel.protectorsList.value!!)
    }

    private fun backToPrevPage() {
        finish()
    }

    private fun closeAddProtegeDialog() {
        Log.d("kyb", "Close offer")
    }

    //뷰모델이 아니라 그냥 dialog 클래스로 가져옴..
    private fun loadAddPage() {
        val dialog = CustomAddProtegeDialog(this)
        dialog.showDialog()
        dialog.setOnClickListener(object: CustomAddProtegeDialog.ButtonClickListener {
            //dialog 추가버튼 클릭시 레트로핏으로 유저정보 전송
            override fun onClicked(email:String, phoneNum:String) {
                viewModel.addProtege(email, phoneNum)
            }
        })
//        val addProtegeDialogView = LayoutInflater.from(this).inflate(R.layout.activity_add_protege, null)
//        val addProtegeDialogBuilder = AlertDialog.Builder(this)
//            .setView(addProtegeDialogView)
//            .setTitle("피보호자 추가")
//
//        val addProtegeAlertDialog = addProtegeDialogBuilder.show()
//        val protegeEmail = addProtegeDialogView.findViewById<EditText>(R.id.protege_email)
//        val protegePhoneNum = addProtegeDialogView.findViewById<EditText>(R.id.protege_phoneNum)
//
//        val addButton = addProtegeDialogView.findViewById<Button>(R.id.addButton)
//        addButton.setOnClickListener {
//            viewModel.addProtege(protegeEmail.text.toString(), protegePhoneNum.text.toString())
//            Toast.makeText(this, "토스트를 굽는 메세지", Toast.LENGTH_SHORT).show()
//        }
//        val closeButton = addProtegeDialogView.findViewById<Button>(R.id.closeButton)
//        closeButton.setOnClickListener {
//            addProtegeAlertDialog.dismiss()
//        }
    }
}