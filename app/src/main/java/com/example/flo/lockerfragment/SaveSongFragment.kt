package com.example.flo.lockerfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flo.R
import com.example.flo.databinding.FragmentSaveSongBinding

class SaveSongFragment : Fragment() {

    lateinit var binding: FragmentSaveSongBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveSongBinding.inflate(inflater , container , false)
        return binding.root
    }


}