package com.example.flo.albumfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.setFragmentResultListener
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.*
import com.example.flo.Data.Album
import com.example.flo.homefragment.HomeFragment
import com.example.flo.adapter.AlbumViewpagerAdapter
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class AlbumFragment : Fragment() {

    //앨범 프래그먼트의 VIewpager2에 songlist프래그먼트로 이어줌
    var _binding:FragmentAlbumBinding? = null
    val binding get() = _binding
    val tablayoutTextArray = arrayListOf("수록곡","상세정보","영상")
    private var gson = Gson()

    private var isLiked: Boolean = false        //앨범이 좋아요 되어있는지 아닌지 체크하는 함수
    lateinit var album: Album

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumBinding.inflate(inflater , container, false)

        val albumData = arguments?.getString("album")
        album = gson.fromJson(albumData, Album::class.java)

        setImageView()
        setViewPager()

        //Home에서 넘어온 데이터 받아오기

        //Home에서 넘어온 데이터를 반영
        isLiked = isLikedAlbum(album.id)            //현재 앨범이 좋아요 눌려있는지 확인
        setInit(album)
        setClickListeners(album)


        //뒤로가기 버튼
        binding?.albumBackIv?.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment(context as MainActivity))
                .commitAllowingStateLoss()
        }


        //HomeFragment에서 넘어온 제목 가수 저장
        setFragmentResultListener("requestKey"){
            requestKey, bundle ->
            val text = arguments?.getString("title")
            val singger = arguments?.getString("singger")

        }


        return binding!!.root
    }

    private fun setClickListeners(album:Album){
        val userId = getUserIdx(requireContext())

        binding?.albumLikeIv?.setOnClickListener {
            if(isLiked){            //좋아요 상태인지 확인
                binding?.albumLikeIv?.setImageResource(R.drawable.ic_my_like_off)
                disLikedAlbum(userId, album.id)
            } else{
                binding?.albumLikeIv?.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId,album.id)
            }
        }
    }

    private fun setInit(album: Album) {
        binding?.albumTitleTv?.text = album.title.toString()
        binding?.albumSinggerTv?.text = album.singer.toString()
        binding?.albumSsumnailIv?.setImageResource(album.coverImg!!)

        if(isLiked){
            binding?.albumLikeIv?.setImageResource(R.drawable.ic_my_like_on)
        } else{
            binding?.albumLikeIv?.setImageResource(R.drawable.ic_my_like_off)
        }
    }


    //이미지뷰 둥글게 셋팅
    fun setImageView(){
        binding!!.albumSsumnailIv.background = resources.getDrawable(R.drawable.round_temp, null)
        binding!!.albumSsumnailIv.clipToOutline = true
    }

    //뷰페이저에 어댑터 설정
    fun setViewPager(){
        binding?.albumViewpager?.adapter = AlbumViewpagerAdapter(this ,album.id)        //앨범 ID가 albumIdx
        binding?.albumViewpager?.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding?.albumViewpager?.offscreenPageLimit = 3

        //TabLayout과 이어주기
        TabLayoutMediator(binding?.albumTabcontainerTl!! , binding?.albumViewpager!!){
            tab,position->
            tab.text = tablayoutTextArray[position]
        }.attach()

    }

    private fun likeAlbum(userId:Int , albumId:Int){        //앨범을 좋아욘 ㅜㄹ렀을 떄 DB에 저장하는 함수
        val songDB = SongDatabase.getInstance(requireContext())!!   //사용자가 눌렀을시 앨범 정보와 사용자 정보 저장
        val like = Like(userId , albumId)

        songDB.albumDao().likeAlbum(like)
    }

    private fun isLikedAlbum(albumId: Int) :Boolean{        //이미 좋아요 되어있는지 아닌지 체크하는 함수
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getUserIdx(requireContext())

        val likeid : Int ? = songDB.albumDao().isLikeAlbum(userId, albumId)  //좋아요가 눌러져있는 앨범인지 아닌지 확이 , 좋아요가 안눌러있으면 결과가 Null
        //LikeTable의 ID값이 리턴됨 -> 자동생성 ,주 키

        //DB에 userId가 albumID 좋아요 누른 적이 없으면 likeid 는 null 따라서 false 리턴 , 있으면 likeid는 숫자 true 리턴
        return likeid != null
    }

    private fun disLikedAlbum(userId: Int , albumId: Int) {        //이미 좋아요 되어있는지 아닌지 체크하는 함수
        val songDB = SongDatabase.getInstance(requireContext())!!
        songDB.albumDao().disLikeAlbum(userId, albumId)
        //좋아요 해제시 테이블에서 값 삭제

    }

    //jwt 가져오기
    private fun getJwt() : Int{     //로그인 했을때 JWT값을 spf 에 저장
        //프래그먼트에선 이렇게 spf 사용
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

        return spf!!.getInt("jwt",0)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}