package com.example.flo.lockerfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.Data.Album
import com.example.flo.R
import com.example.flo.SongDatabase
import com.example.flo.adapter.LockerRVAdapter
import com.example.flo.adapter.SaveAlbumRVAdapter
import com.example.flo.databinding.FragmentSaveSongBinding
import com.example.flo.databinding.FragmentSavedAlbumBinding
import com.example.flo.getUserIdx

class SavedAlbumFragment : Fragment() {

    lateinit var binding : FragmentSavedAlbumBinding
    lateinit var songDB : SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedAlbumBinding.inflate(inflater , container , false)
        songDB = SongDatabase.getInstance(requireContext())!!
        val saveAlbumRVAdapter = SaveAlbumRVAdapter()
        binding.savealbumRecyclerviewRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL , false)
        binding.savealbumRecyclerviewRv.adapter = saveAlbumRVAdapter
        var userId = getUserIdx(requireContext())
        saveAlbumRVAdapter.addAlbums(songDB.albumDao().getLikeAlbum(userId) as ArrayList<Album>)

        return binding.root
    }

}