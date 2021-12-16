package com.example.flo.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AlbumRetrofitInterface {
    @GET("/albums")
    fun getAlbums():Call<AlbumsResponse>

    @GET("/albums/{albumIdx}")
    fun getSongsInAlbums(@Path("albumIdx") albumIdx:Int) : Call<SongsInAlbumResponse>
}