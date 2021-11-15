package com.example.flo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.Data.Album
import com.example.flo.Data.LockerSong
import com.example.flo.Data.Song
import com.example.flo.databinding.ItemAlbumBinding
import com.example.flo.databinding.ItemLockerBinding
import java.util.concurrent.locks.Lock

class LockerRVAdapter() : RecyclerView.Adapter<LockerRVAdapter.ViewHolder>() {

    private val songList = ArrayList<Song>()

    //클릭 인터페이스 정의
    interface MyItemClickListener{
        //클릭시 삭제
        fun onRemoveSong(songId: Int)

    }

    //리스너 객체를 전달받는 함수와 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener : MyItemClickListener       //리스너 객체 저장할 변수

    fun setMyItemClickListener(itemClickLister:MyItemClickListener){
        mItemClickListener = itemClickLister                            //리스너 객체를 전달받아서 리스너 객체 변수에 저장
    }

    //뷰홀더를 생성해줘야 할 때 호출되는 함수 -> 아이템 뷰 객체를 만들어서 뷰홀더에 넣어줌
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LockerRVAdapter.ViewHolder {
        val binding :ItemLockerBinding = ItemLockerBinding.inflate(LayoutInflater.from(viewGroup.context) , viewGroup , false)
        return ViewHolder(binding)
    }

    // 뷰 홀더에 데이터를 ㅏ인딩해줘야 할 때마다 호출되는 함수 -> 엄청나게 많이 호출
    override fun onBindViewHolder(holder: LockerRVAdapter.ViewHolder, position: Int) {
        holder.bind(songList[position])
        holder.binding.itemLockerRemoveIv.setOnClickListener{
            mItemClickListener.onRemoveSong(songList[position].id)
            removeItem(position)

        }      //리스너에 있는 ItemClick 실행
    }

    //데이터 세트 크기를 알려주는 함수 -> 리사이클러뷰가 마지막이 언제인지를 알게 된다.
    override fun getItemCount(): Int = songList.size

    //뷰 홀더 -> 아이템 객체들을 재활용하기 위한 뷰 , 아이테 객체들을 담는 뷰
    inner class ViewHolder(val binding:ItemLockerBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(song: Song){
            binding.itemLockerTitleTv.text = song.title
            binding.itemLockerSingerTv.text = song.singger
            binding.itemLockerCoverIv.setImageResource(song.coverImg!!)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs:ArrayList<Song>){
        this.songList.clear()
        this.songList.addAll(songs)

        notifyDataSetChanged()
    }

    fun addItem(song: Song){
        songList.add(song)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        songList.removeAt(position)
        notifyDataSetChanged()
    }
}