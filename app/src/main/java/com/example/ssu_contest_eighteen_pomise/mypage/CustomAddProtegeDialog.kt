package com.example.ssu_contest_eighteen_pomise.mypage

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.ssu_contest_eighteen_pomise.R

class CustomAddProtegeDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var onClickListener:ButtonClickListener

    interface ButtonClickListener {
        fun onClicked(email:String, phoneNum:String)
    }

    fun setOnClickListener(listener:ButtonClickListener) {
        onClickListener = listener
    }

    fun showDialog() {
        dialog.setContentView(R.layout.activity_add_protege)

        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.setTitle("제목") //이거 동작안함.

        //데이터바인딩 못쓰겠음.
        val protegeEmail = dialog.findViewById<EditText>(R.id.add_protege_email)
        val protegePhoneNum = dialog.findViewById<EditText>(R.id.add_protege_phoneNum)
        val addBtn = dialog.findViewById<Button>(R.id.addButton)
        val closeBtn = dialog.findViewById<Button>(R.id.closeButton)

        addBtn.setOnClickListener {
            Log.d("kyb", "addBtn clicked")
            onClickListener.onClicked(protegeEmail.text.toString(), protegePhoneNum.text.toString())
            dialog.dismiss()
        }

        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}