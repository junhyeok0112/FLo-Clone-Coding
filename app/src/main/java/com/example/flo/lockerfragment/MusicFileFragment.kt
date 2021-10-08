package com.example.flo.lockerfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flo.R
import com.example.flo.databinding.FragmentMusicFileBinding

class MusicFileFragment : Fragment() {

    lateinit var binding : FragmentMusicFileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMusicFileBinding.inflate(inflater , container,  false)

        binding.musicfileAllSelectContainer.setOnClickListener {
            changeSelect()
        }

        return binding.root
    }

    fun changeSelect(){
        if(binding.musicfileSelectTv.text == "전체선택"){
            binding.musicfileSelectIv.setImageResource(R.drawable.btn_playlist_select_on)
            binding.musicfileSelectTv.text = "선택해제"
        } else{
            binding.musicfileSelectIv.setImageResource(R.drawable.btn_playlist_select_off)
            binding.musicfileSelectTv.text = "전체선택"
        }
    }

}