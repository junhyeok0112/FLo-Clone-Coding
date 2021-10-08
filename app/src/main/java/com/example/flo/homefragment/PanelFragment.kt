package com.example.flo.homefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flo.R
import com.example.flo.databinding.FragmentPanelBinding


class PanelFragment(val imgRes:Int) : Fragment() {

    lateinit var binding : FragmentPanelBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPanelBinding.inflate(inflater , container , false)
        binding.panelBackgroudIv.setImageResource(imgRes)

        return binding.root
    }


}