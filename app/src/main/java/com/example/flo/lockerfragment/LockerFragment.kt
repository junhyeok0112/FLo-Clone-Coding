package com.example.flo.lockerfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.adapter.LockerViewpagerAdapter
import com.example.flo.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator

//import com.example.flo.databinding.FragmentLockerBinding


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    val tabList = arrayListOf("저장한 곡", "음악파일")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)
        setViewpager()
        return binding.root
    }


    fun setViewpager(){
        val lockerViewpagerAdapter = LockerViewpagerAdapter(this)
        binding.lockerViewpager2Vp.adapter = lockerViewpagerAdapter
        binding.lockerViewpager2Vp.offscreenPageLimit = 2
        TabLayoutMediator(binding.lockerTablayoutTl , binding.lockerViewpager2Vp){
            tab,position->
            tab.text = tabList[position]
        }.attach()

    }
}