package com.example.flo.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.albumfragment.DetailFragment
import com.example.flo.albumfragment.SongListFragment
import com.example.flo.albumfragment.VideoFragment

class AlbumViewpagerAdapter(fragment: Fragment ,val albumIdx : Int) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->SongListFragment(albumIdx)
            1->DetailFragment()
            else->VideoFragment()
        }
    }
}