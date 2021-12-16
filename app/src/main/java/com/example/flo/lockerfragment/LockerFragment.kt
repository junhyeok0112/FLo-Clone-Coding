package com.example.flo.lockerfragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo.login.LoginActivity
import com.example.flo.MainActivity
import com.example.flo.SongDatabase
import com.example.flo.adapter.LockerViewpagerAdapter
import com.example.flo.databinding.FragmentLockerBinding
import com.example.flo.getJwt
import com.example.flo.getUserIdx
import com.google.android.material.tabs.TabLayoutMediator

//import com.example.flo.databinding.FragmentLockerBinding


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    val tabList = arrayListOf("저장한 곡", "음악파일" , "저장한 앨범")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)
        setViewpager()


        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initView()
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

    //여기서부터 이제 자동로그인 , 보관함 갔을떄 Jwt 이용해서 하는거 해야하지만, 지금은 UserIndex로
    private fun initView(){
        //로그인 되어있으면 jwt값이 0이 아닌 다른값으로 존재한다
        val jwt = getJwt(requireContext()) //jwt를 가져오는 함수

        if(jwt == ""){  //로그아웃시
            binding.lockerLoginTv.text = "로그인"

            binding.lockerLoginTv.setOnClickListener {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        } else{     //로그인 되어 있으면
            binding.lockerLoginTv.text = "로그아웃"
            val songDb = SongDatabase.getInstance(requireContext())!!
            //binding.lockerNameTv.text = songDb.userDao().getName(jwt)


            binding.lockerLoginTv.setOnClickListener {
                //로그아웃 시켜주는 함수
                logout()
                startActivity(Intent(activity,MainActivity::class.java))
            }
        }
    }


    private fun logout(){           //spf의 jwt 값을 0으로만 바꿔주면 됨 -> 그러면 자연스럽게 다시 시작하면 0 으로 셋팅
        val spf = activity?.getSharedPreferences("auth",AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()

        editor.remove("jwt")
        editor.apply()
    }

}