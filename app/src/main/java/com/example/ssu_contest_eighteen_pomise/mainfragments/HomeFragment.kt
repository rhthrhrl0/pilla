package com.example.ssu_contest_eighteen_pomise.mainfragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.databinding.FragmentHomeBinding
import com.example.ssu_contest_eighteen_pomise.mainfragments.list.DetailAlarmActivity
import com.example.ssu_contest_eighteen_pomise.mainfragments.list.PillListAdapter
import com.example.ssu_contest_eighteen_pomise.mainfragments.patient_list.PatientListAdapter
import com.yourssu.design.system.atom.ToolTip

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeFragmentViewModel by viewModels()
    private val patientListAdapter=PatientListAdapter()
    private val adapter=PillListAdapter()
    var tooltipBuilders: ToolTip.Builder? = null

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

        //툴팁 빌더 초기화.
        context?.let {
            activity?.let { it1 ->
                tooltipBuilders =
                    ToolTip.Builder(context = it, it1.windowManager, inflater)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.patientListRv.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.patientListRv.adapter=patientListAdapter

        binding.pillListRv.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.pillListRv.adapter=adapter
        binding.refreshLayout.setColorSchemeColors(resources.getColor(R.color.mainColor))

        Log.d("kmj","뷰모델옵저버 세팅")
        onViewModelInit()
        Log.d("kmj","뷰모델옵저버 세팅 끝")

        onInitView()

    }

    fun onViewModelInit(){
        viewModel.refreshStartEvent.observe(viewLifecycleOwner,{
            binding.refreshLayout.isRefreshing=true
        })

        viewModel.patientListItems.observe(viewLifecycleOwner,{
            if(it.isNotEmpty()){
                patientListAdapter.updateItems(it)
            }
        })

        viewModel.curPatientEmail.observe(viewLifecycleOwner,{
            viewModel.getListItem(true)
        })

        viewModel.refreshEndEvent.observe(viewLifecycleOwner,{
            binding.refreshLayout.isRefreshing=false
            adapter.updateItems(viewModel.pillListItems.value?: emptyList())
            Toast.makeText(context,"알람목록을 갱신했습니다.", Toast.LENGTH_SHORT).show()
        })

        viewModel.refreshInitWithViewModel.observe(viewLifecycleOwner,{
            binding.refreshLayout.isRefreshing=false
            Log.d("kmj","얻는함수의 시간 집합: ${viewModel.pillListItems.value?: emptyList()}")
            adapter.updateItems(viewModel.pillListItems.value?: emptyList())
        })
    }

    fun onInitView(){
        adapter.setMyItemClickListener(object :PillListAdapter.MyItemClickListener{
            override fun onItemClick(position: Int) {
                Log.d("kmj","${position}번쨰입니다.")
                val intent = Intent(activity, DetailAlarmActivity::class.java)
                intent.putExtra(DetailAlarmActivity.KEY_PILL_NAME,viewModel.tooltipString(position))
                startActivity(intent)
            }

            override fun onMorePillInfoClick(position: Int, view: View) {
                val toolTip: ToolTip =
                    tooltipBuilders
                        ?.withIsNormal(true)
                        ?.withStringContents(viewModel.tooltipString(position))
                        ?.withHopeLocation(ToolTip.HopeLocation.BELOW)
                        ?.withToastLength(
                            ToolTip.Length.LONG
                        )!!.build(view)

                toolTip.show()
            }

            override fun onItemLongClick(position: Int) {

            }
        })

        patientListAdapter.setMyItemClickListener(object :PatientListAdapter.MyItemClickListener{
            override fun onItemClick(position: Int, email: String) {
                Log.d("kmj","$position,$email")
                viewModel.clickPatientIndex(position,email)
            }
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