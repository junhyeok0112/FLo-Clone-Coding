package com.example.flo.retrofit

import com.example.flo.Data.Song

data class SongsInAlbumResponse(       //이거 재활용 안됨
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: ArrayList<Song>?
)