package com.example.flo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo.Data.Song
import com.example.flo.databinding.ItemAlbumInSongListBinding
import com.example.flo.databinding.ItemLockerBinding

class SongListRVAdapter(val context: Context) : RecyclerView.Adapter<SongListRVAdapter.ViewHolder>() {

    private val songList = ArrayList<Song>()

    //뷰홀더를 생성해줘야 할 때 호출되는 함수 -> 아이템 뷰 객체를 만들어서 뷰홀더에 넣어줌
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SongListRVAdapter.ViewHolder {
        val binding : ItemAlbumInSongListBinding = ItemAlbumInSongListBinding.inflate(LayoutInflater.from(viewGroup.context) , viewGroup , false)
        return ViewHolder(binding)
    }

    // 뷰 홀더에 데이터를 ㅏ인딩해줘야 할 때마다 호출되는 함수 -> 엄청나게 많이 호출
    override fun onBindViewHolder(holder: SongListRVAdapter.ViewHolder, position: Int) {
        holder.bind(songList[position])
    }

    //데이터 세트 크기를 알려주는 함수 -> 리사이클러뷰가 마지막이 언제인지를 알게 된다.
    override fun getItemCount(): Int = songList.size

    //뷰 홀더 -> 아이템 객체들을 재활용하기 위한 뷰 , 아이테 객체들을 담는 뷰
    inner class ViewHolder(val binding: ItemAlbumInSongListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(song: Song){
            binding.itemSonglistNumberTv.text = song.id.toString()
            binding.itemSonglistTitleTv.text = song.title
            binding.itemSonglistSinggerTv.text = song.singger
            if(song.isTitleSong != "T"){
                binding.itemSonglistTitlebtnTv.visibility = View.GONE
            }

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