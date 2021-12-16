package com.example.flo.albumfragment

import com.example.flo.Data.Song

interface SongListView {
    fun onSongListSuccess(songs:ArrayList<Song>)
    fun onSongListFailure(code:Int , message :String)
}