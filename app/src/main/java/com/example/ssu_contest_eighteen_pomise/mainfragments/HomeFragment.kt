package com.example.ssu_contest_eighteen_pomise.mainfragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ssu_contest_eighteen_pomise.MainViewModel
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.FragmentHomeBinding
import com.example.ssu_contest_eighteen_pomise.mainfragments.list.PillListAdapter

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeFragmentViewModel by viewModels()
    private val adapter=PillListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner=this@HomeFragment
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pillListRv.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.pillListRv.adapter=adapter
        binding.refreshLayout.setColorSchemeColors(resources.getColor(R.color.mainColor))

        Log.d("kmj","뷰모델옵저버 세팅")
        onViewModelInit()
        Log.d("kmj","뷰모델옵저버 세팅 끝")
        //viewModel.refreshBtn()
    }

    fun onViewModelInit(){
        viewModel.refreshStartEvent.observe(viewLifecycleOwner,{
            binding.refreshLayout.isRefreshing=true
        })

        viewModel.refreshEndEvent.observe(viewLifecycleOwner,{
            binding.refreshLayout.isRefreshing=false
            adapter.updateItems(viewModel.pillListItems.value?: emptyList())
            Toast.makeText(context,"알람목록을 갱신했습니다.", Toast.LENGTH_SHORT).show()
        })

        viewModel.refreshInitWithViewModel.observe(viewLifecycleOwner,{
            binding.refreshLayout.isRefreshing=false
            adapter.updateItems(viewModel.pillListItems.value?: emptyList())
        })
    }






    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}