package com.example.flo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentAlbumBinding


class AlbumFragment : Fragment() {

    //앨범 프래그먼트의 VIewpager2에 songlist프래그먼트로 이어줌
    var _binding:FragmentAlbumBinding? = null
    val binding get() = _binding

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
        val text = arguments?.getString("title")
        val singger = arguments?.getString("singger")
        binding?.albumTitleTv?.text = text
        binding?.albumSinggerTv?.text = singger

        return binding!!.root
    }


    //뷰페이저 어댑터 지정
    inner class ViewPagerAdapter : FragmentStateAdapter(this){
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0->{
                    SongListFragment()
                }
                1->{
                    SongListFragment()
                }
                2->{
                    SongListFragment()
                }
                else->{
                    SongListFragment()
                }
            }
        }
    }

    //이미지뷰 둥글게 셋팅
    fun setImageView(){
        binding!!.albumSsumnailIv.background = resources.getDrawable(R.drawable.round_temp , null)
        binding!!.albumSsumnailIv.clipToOutline = true
    }

    //뷰페이저에 어댑터 설정
    fun setViewPager(){
        binding?.albumViewpager?.adapter = ViewPagerAdapter()
        binding?.albumViewpager?.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding?.albumViewpager?.offscreenPageLimit = 3
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}