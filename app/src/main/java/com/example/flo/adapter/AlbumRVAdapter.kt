package com.example.flo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.Data.Album
import com.example.flo.databinding.ItemAlbumBinding

class AlbumRVAdapter(private val albumList : ArrayList<Album>) : RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    //클릭 인터페이스 정의
    interface MyItemClickListener{
        fun onItemClick(album: Album)
        fun onStartSong(position:Int)      //누르면 미니 플레이어에 노래 셋팅
    }

    //리스너 객체를 전달받는 함수와 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener : MyItemClickListener       //리스너 객체 저장할 변수

    fun setMyItemClickListener(itemClickLister:MyItemClickListener){
        mItemClickListener = itemClickLister                            //리스너 객체를 전달받아서 리스너 객체 변수에 저장
    }

    //뷰홀더를 생성해줘야 할 때 호출되는 함수 -> 아이템 뷰 객체를 만들어서 뷰홀더에 넣어줌
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRVAdapter.ViewHolder {
        val binding :ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context) , viewGroup , false)
        return ViewHolder(binding)
    }

    // 뷰 홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 -> 엄청나게 많이 호출
    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener{ mItemClickListener.onStartSong(position) }      //리스너에 있는 onStartSong 실행 , 임시
    }

    //데이터 세트 크기를 알려주는 함수 -> 리사이클러뷰가 마지막이 언제인지를 알게 된다.
    override fun getItemCount(): Int = albumList.size

    //뷰 홀더 -> 아이템 객체들을 재활용하기 위한 뷰 , 아이테 객체들을 담는 뷰
    inner class ViewHolder(val binding:ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album){
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverIv.setImageResource(album.coverImg!!)
        }
    }

    fun addItem(album: Album){
        albumList.add(album)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }
}