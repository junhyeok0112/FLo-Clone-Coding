package com.example.flo.homefragment

import com.example.flo.Data.Album
import com.example.flo.Data.Song

interface HomeView {
    fun onGetAlbumsLoading()
    fun onGetAlbumsSuccess(albums:ArrayList<Album>)
    fun onGetAlbumsFailure(code:Int, message:String)
}