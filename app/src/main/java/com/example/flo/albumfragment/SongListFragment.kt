package com.example.flo.albumfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.Data.Song
import com.example.flo.adapter.LockerRVAdapter
import com.example.flo.adapter.SongListRVAdapter
import com.example.flo.databinding.FragmentSongListBinding
import com.example.flo.retrofit.AlbumService

class SongListFragment(val albumIdx : Int) : Fragment() ,SongListView{
    //앨범 인덱스를 받아옴
    //앨범 프래그먼트에서 이으려면 SongList 프래그먼트랑 이어야함
    var _binding : FragmentSongListBinding? = null
    val binding get() = _binding

    lateinit var adapter : SongListRVAdapter  //노래 어댑터 재활용

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSongListBinding.inflate(inflater , container , false)

        adapter = SongListRVAdapter(requireContext())
        binding?.songlistRecyclerRv?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding?.songlistRecyclerRv?.adapter = adapter
       //AlbumIndex 받아서 Retorfit 호출, songResponse 이용
        val albumService = AlbumService()
        albumService.setSongListView(this)

        albumService.getSongInAlbum(albumIdx)

        //토글 클릭시 변경
        binding?.songlistToggleIv?.setOnClickListener {
            setChangToggle(false)
        }

        binding?.songlistToggleOnIv?.setOnClickListener {
            setChangToggle(true)
        }



        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setChangToggle(isOn : Boolean){
        when(isOn){
            true->{
                binding?.songlistToggleIv?.visibility = View.VISIBLE
                binding?.songlistToggleOnIv?.visibility = View.GONE
            }
            false->{
                binding?.songlistToggleIv?.visibility = View.GONE
                binding?.songlistToggleOnIv?.visibility = View.VISIBLE
            }
        }
    }

    override fun onSongListSuccess(songs: ArrayList<Song>) {
        //어댑터 연결 Song들
        adapter.addSongs(songs)
    }

    override fun onSongListFailure(code: Int, message: String) {
        when(code){
            400->{
                Log.d("SONGLIST-ERROR" ,message)
            }
        }
    }
}