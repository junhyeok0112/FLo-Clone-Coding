package com.example.flo.retrofit

import com.example.flo.Data.Song

data class SongResult(val songs:ArrayList<Song>)

data class SongsResponse(       //이거 재활용 안됨
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: SongResult?
)