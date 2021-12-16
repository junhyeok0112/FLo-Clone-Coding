package com.example.flo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.Data.Album
import com.example.flo.Data.Song
import com.example.flo.databinding.ItemLockerBinding
import com.example.flo.databinding.ItemSaveAlbumBinding

class SaveAlbumRVAdapter() : RecyclerView.Adapter<SaveAlbumRVAdapter.ViewHolder>() {
    private val albumList = ArrayList<Album>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SaveAlbumRVAdapter.ViewHolder {
        val binding : ItemSaveAlbumBinding = ItemSaveAlbumBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SaveAlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding:ItemSaveAlbumBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album){
            binding.savealbumTitleTv.text = album.title
            binding.savealbumSinggerTv.text = album.singer
            binding.savealbumCoverIv.setImageResource(album.coverImg!!)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAlbums(albums:ArrayList<Album>){
        this.albumList.clear()
        this.albumList.addAll(albums)

        notifyDataSetChanged()
    }
}