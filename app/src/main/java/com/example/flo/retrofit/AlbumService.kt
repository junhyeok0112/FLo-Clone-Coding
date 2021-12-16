package com.example.flo.retrofit

import android.util.Log
import com.example.flo.albumfragment.SongListView
import com.example.flo.homefragment.HomeView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumService {
    private lateinit var homeView: HomeView
    private lateinit var songListView: SongListView

    fun setHomeView(homeView: HomeView){
        this.homeView = homeView
    }

    fun setSongListView(songListView: SongListView){
        this.songListView = songListView
    }

    //API 호출하고 관리하는 메서드 호출 , 이 안에서 직접적으로 API 호출
    fun getAlbums(){
        val albumService = getRetorfit().create(AlbumRetrofitInterface::class.java)

        homeView.onGetAlbumsLoading()            //호출 전에 로딩시켜서 기다리게함

        albumService.getAlbums().enqueue(object : Callback<AlbumsResponse> {

            override fun onResponse(call: Call<AlbumsResponse>, response: Response<AlbumsResponse>) {
                Log.d("SONGSERVICE/APIRESPONSE" , response.toString())       //네트워크에서 보내준 응답값

                //서버가 보내준 데이터 파싱하려면 response.body()사용
                val resp = response.body()!!

                Log.d("ALBUMSERVICE/RESPONSE" , resp.toString())
                when(resp.code){
                    1000 -> homeView.onGetAlbumsSuccess(resp.result!!.albums)      //여기서 받아서 액티비티와 연결된 인터페이스 메서드를 이용해서 처리
                    //Result안에 songs에 리스트 있으므로 전달
                    else -> homeView.onGetAlbumsFailure(resp.code , resp.message)
                }
            }

            override fun onFailure(call: Call<AlbumsResponse>, t: Throwable) {
                Log.d("ALBUMSERVICE/API-ERROR" , t.message.toString())
                homeView.onGetAlbumsFailure(400 , "네트워크 오류가 발생하였습니다.")
            }
        })
    }

    fun getSongInAlbum(albumIdx: Int){
        val albumService = getRetorfit().create(AlbumRetrofitInterface::class.java)

        albumService.getSongsInAlbums(albumIdx).enqueue(object : Callback<SongsInAlbumResponse> {

            override fun onResponse(call: Call<SongsInAlbumResponse>, response: Response<SongsInAlbumResponse>) {
                Log.d("SONGSERVICE/APIRESPONSE" , response.toString())       //네트워크에서 보내준 응답값

                //서버가 보내준 데이터 파싱하려면 response.body()사용
                val resp = response.body()!!

                Log.d("SONGLIST/RESPONSE" , resp.toString())
                when(resp.code){
                    1000 -> songListView.onSongListSuccess(resp.result!!)      //여기서 받아서 액티비티와 연결된 인터페이스 메서드를 이용해서 처리
                    //Result안에 songs에 리스트 있으므로 전달
                    else -> songListView.onSongListFailure(resp.code , resp.message)
                }
            }

            override fun onFailure(call: Call<SongsInAlbumResponse>, t: Throwable) {   //이게 호출되면 retrofit 실수
                Log.d("SONGLIST/API-ERROR" , t.message.toString())
                songListView.onSongListFailure(400 , "네트워크 오류가 발생하였습니다.")
            }
        })
    }
}