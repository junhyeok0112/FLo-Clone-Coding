package com.example.flo.retrofit

import com.example.flo.Data.Album


data class AlbumResult(val albums:ArrayList<Album>)

data class AlbumsResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: AlbumResult?
)
