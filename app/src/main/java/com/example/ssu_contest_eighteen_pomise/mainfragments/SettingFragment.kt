package com.example.ssu_contest_eighteen_pomise.mainfragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.ssu_contest_eighteen_pomise.App
import com.example.ssu_contest_eighteen_pomise.mypage.SettingAlarmActivity
import com.example.ssu_contest_eighteen_pomise.mypage.SettingMealtimeActivity
import com.example.ssu_contest_eighteen_pomise.mypage.SettingGuardiansActivity
import com.example.ssu_contest_eighteen_pomise.auth.LoginActivity
import com.example.ssu_contest_eighteen_pomise.databinding.FragmentSettingBinding
import com.example.ssu_contest_eighteen_pomise.extensionfunction.slideRightEnterAndNone
import com.example.ssu_contest_eighteen_pomise.mypage.SettingProtegesActivity


class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private val viewModel: SettingFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewModel.getEmail()

        viewModel.logoutVar.observe(viewLifecycleOwner, {
            logoutAndStartLoginActivity()
        })

        viewModel.failedLogoutToast.observe(viewLifecycleOwner, {
            Toast.makeText(context, "로그아웃에 실패했습니다", Toast.LENGTH_SHORT).show()
        })

        viewModel.startSettingAlarmActivity.observe(viewLifecycleOwner, {
            startSettingAlarmActivity()
        })

        viewModel.startSettingMealtimeActivity.observe(viewLifecycleOwner, {
            startSettingMealtimeActivity()
        })

        viewModel.startSettingGuardiansActivity.observe(viewLifecycleOwner, {
            startSettingGuardiansActivity()
        })

        viewModel.startSettingProtegesActivity.observe(viewLifecycleOwner, {
            startSettingProtegesActivity()
        })
    }

    private fun startSettingProtegesActivity() {
        val intent = Intent(activity, SettingProtegesActivity::class.java)
        startActivity(intent)
        activity?.slideRightEnterAndNone()
    }

    fun startSettingGuardiansActivity() {
        val intent = Intent(activity, SettingGuardiansActivity::class.java)
        startActivity(intent)
        activity?.slideRightEnterAndNone()
    }

    fun startSettingMealtimeActivity() {
        val intent = Intent(activity, SettingMealtimeActivity::class.java)
        startActivity(intent)
        activity?.slideRightEnterAndNone()
    }

    fun startSettingAlarmActivity() {
        val intent = Intent(activity, SettingAlarmActivity::class.java)
        startActivity(intent)
        activity?.slideRightEnterAndNone()
    }

    fun logoutAndStartLoginActivity() {
        val shPre = App.token_prefs
        shPre.refreshToken = ""
        shPre.accessToken = ""
        shPre.name = ""
        shPre.email = ""
        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}