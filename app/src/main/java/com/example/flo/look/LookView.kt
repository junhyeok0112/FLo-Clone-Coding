package com.example.flo.look

import com.example.flo.Data.Song

interface LookView {            //Fragment와 Retrofit 서비스간 전달을 위한 인터페이스
    fun onGetSongsLoading()
    fun onGetSongsSuccess(songs:ArrayList<Song>)
    fun onGetSongsFailure(code:Int, message:String)
}