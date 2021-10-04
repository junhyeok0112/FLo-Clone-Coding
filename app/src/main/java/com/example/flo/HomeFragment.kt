package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentHomeBinding
//import com.example.flo.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        //오늘 앨범 누르면 이동
        binding.homeTodayAlbum1Container.setOnClickListener {
            moveAlbumFragment(1)
        }

        binding.homeTodayAlbum2Container.setOnClickListener {
            moveAlbumFragment(2)
        }

        binding.homeTodayAlbum3Container.setOnClickListener {
            moveAlbumFragment(3)
        }

        binding.homeTodayAlbum4Container.setOnClickListener {
            moveAlbumFragment(4)
        }




        return binding.root
    }


    fun moveAlbumFragment(num:Int){        //앨범 눌렀을때 제목과 가수를 album_fragment로 보내기
        val song = Song()
        when(num){
            1->{
                song.title = binding.homeTodayAlbum1Title.text.toString()
                song.singger = binding.homeTodayAlbum1Singger.text.toString()
            }
            2->{
                song.title = binding.homeTodayAlbum2Title.text.toString()
                song.singger = binding.homeTodayAlbum2Singger.text.toString()
            }
            3->{
                song.title = binding.homeTodayAlbum3Title.text.toString()
                song.singger = binding.homeTodayAlbum3Singger.text.toString()
            }
            4->{
                song.title = binding.homeTodayAlbum4Title.text.toString()
                song.singger = binding.homeTodayAlbum4Singger.text.toString()
            }
        }
        //song.imgId = binding.homeTodayAlbum1Ssumnail.sourceLayoutResId.toString()
        val bundle = Bundle()
        bundle.putString("title" , song.title)
        bundle.putString("singger",  song.singger)

        val albumFragment = AlbumFragment()
        albumFragment.arguments = bundle

        (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, albumFragment)
            .commitAllowingStateLoss()

    }
}