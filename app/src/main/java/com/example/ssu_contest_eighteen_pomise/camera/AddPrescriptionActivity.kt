package com.example.ssu_contest_eighteen_pomise.camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityAddPrescriptionBinding

class AddPrescriptionActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddPrescriptionBinding
    private val viewModel:AddPrescriptionViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_add_prescription)
        binding.lifecycleOwner=this
        binding.viewModel=viewModel

    }
}