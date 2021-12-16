package com.example.flo.look

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.Data.Song
import com.example.flo.adapter.LockerRVAdapter
import com.example.flo.databinding.FragmentLookBinding
import com.example.flo.retrofit.SongService

//import com.example.flo.databinding.FragmentLookBinding


class LookFragment : Fragment() , LookView{

    lateinit var binding: FragmentLookBinding
    private lateinit var lockerRVAdapter: LockerRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLookBinding.inflate(inflater, container, false)

        initRecyclerView()
        getSongs()

        return binding.root
    }

    private fun initRecyclerView(){
        lockerRVAdapter = LockerRVAdapter(requireContext())
        binding.lookFloChartRv.adapter = lockerRVAdapter     //어짜피 LokcerRV와 같은 형식으로 나오므로 재활용
    }

    private fun getSongs(){
        val songService = SongService()
        songService.setLookView(this)

        songService.getSongs()
    }

    override fun onGetSongsLoading() {
        binding.lookLoadingPb.visibility = View.VISIBLE
    }

    override fun onGetSongsSuccess(songs:ArrayList<Song>) {
        binding.lookLoadingPb.visibility = View.GONE

        lockerRVAdapter.addSongs(songs)
    }

    override fun onGetSongsFailure(code: Int, message: String) {
        binding.lookLoadingPb.visibility = View.GONE

        when(code){
            400 -> Log.d("LOOKFRAG/API-ERROR", message)
        }
    }
}