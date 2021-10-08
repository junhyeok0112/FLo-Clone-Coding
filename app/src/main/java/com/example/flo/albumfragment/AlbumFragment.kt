package com.example.flo.albumfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.homefragment.HomeFragment
import com.example.flo.MainActivity
import com.example.flo.R
import com.example.flo.adapter.AlbumViewpagerAdapter
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator


class AlbumFragment : Fragment() {

    //앨범 프래그먼트의 VIewpager2에 songlist프래그먼트로 이어줌
    var _binding:FragmentAlbumBinding? = null
    val binding get() = _binding
    val tablayoutTextArray = arrayListOf("수록곡","상세정보","영상")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumBinding.inflate(inflater , container, false)
        setImageView()
        setViewPager()

        //뒤로가기 버튼
        binding?.albumBackIv?.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }


        //HomeFragment에서 넘어온 제목 가수 저장
        setFragmentResultListener("requestKey"){
            requestKey, bundle ->
            val text = arguments?.getString("title")
            val singger = arguments?.getString("singger")
            binding?.albumTitleTv?.text = bundle.getString("title")
            binding?.albumSinggerTv?.text = bundle.getString("singger")
            binding?.albumSsumnailIv?.setImageBitmap(bundle.getParcelable("imgRes"))
        }


        return binding!!.root
    }


    //이미지뷰 둥글게 셋팅
    fun setImageView(){
        binding!!.albumSsumnailIv.background = resources.getDrawable(R.drawable.round_temp, null)
        binding!!.albumSsumnailIv.clipToOutline = true
    }

    //뷰페이저에 어댑터 설정
    fun setViewPager(){
        binding?.albumViewpager?.adapter = AlbumViewpagerAdapter(this)
        binding?.albumViewpager?.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding?.albumViewpager?.offscreenPageLimit = 3

        //TabLayout과 이어주기
        TabLayoutMediator(binding?.albumTabcontainerTl!! , binding?.albumViewpager!!){
            tab,position->
            tab.text = tablayoutTextArray[position]
        }.attach()

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}