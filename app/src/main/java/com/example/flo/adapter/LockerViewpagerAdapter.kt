package com.example.flo.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.lockerfragment.MusicFileFragment
import com.example.flo.lockerfragment.SaveSongFragment
import com.example.flo.lockerfragment.SavedAlbumFragment

class LockerViewpagerAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> SaveSongFragment()
            1-> MusicFileFragment()
            else->SavedAlbumFragment()
        }
    }

}