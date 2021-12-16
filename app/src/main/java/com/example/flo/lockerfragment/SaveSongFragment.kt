package com.example.flo.lockerfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.Data.Album
import com.example.flo.Data.LockerSong
import com.example.flo.Data.Song
import com.example.flo.R
import com.example.flo.SongDatabase
import com.example.flo.adapter.LockerRVAdapter
import com.example.flo.databinding.FragmentSaveSongBinding

class SaveSongFragment : Fragment() {

    lateinit var binding: FragmentSaveSongBinding
    lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveSongBinding.inflate(inflater , container , false)
        songDB = SongDatabase.getInstance(requireContext())!!

        val lockerSongAdapter = LockerRVAdapter(requireContext())
        binding.savesongRecyclerRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL , false)
        lockerSongAdapter.setMyItemClickListener(object : LockerRVAdapter.MyItemClickListener{
            override fun onRemoveSong(songId: Int) {
                songDB.songDao().updateIsById(false, songId)                //Like 해제 하고 리스트에서 삭제
            }
        })

        binding.savesongRecyclerRv.adapter = lockerSongAdapter
        lockerSongAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList)
        Log.d("locker","${lockerSongAdapter.itemCount}")


        return binding.root
    }


}