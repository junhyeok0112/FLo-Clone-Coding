package com.example.flo.retrofit

import android.util.Log
import com.example.flo.User
import com.example.flo.look.LookView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SongService {     //AuthService같이 Song관련 ,
    private lateinit var lookView: LookView

    fun setLookView(lookView: LookView){
        this.lookView = lookView
    }

    //API 호출하고 관리하는 메서드 호출 , 이 안에서 직접적으로 API 호출
    fun getSongs(){
        val songService = getRetorfit().create(SongRetrofitInterface::class.java)

        lookView.onGetSongsLoading()            //호출 전에 로딩시켜서 기다리게함

        songService.getSongs().enqueue(object : Callback<SongsResponse> {

            override fun onResponse(call: Call<SongsResponse>, response: Response<SongsResponse>) {
                Log.d("SONGSERVICE/APIRESPONSE" , response.toString())       //네트워크에서 보내준 응답값

                //서버가 보내준 데이터 파싱하려면 response.body()사용
                val resp = response.body()!!

                Log.d("SONGSERVICE/APIRESPONSE" , resp.toString())
                when(resp.code){
                    1000 -> lookView.onGetSongsSuccess(resp.result!!.songs)      //여기서 받아서 액티비티와 연결된 인터페이스 메서드를 이용해서 처리
                    //Result안에 songs에 리스트 있으므로 전달
                    else -> lookView.onGetSongsFailure(resp.code , resp.message)
                }
            }

            override fun onFailure(call: Call<SongsResponse>, t: Throwable) {
                Log.d("SONGSERVICE/API-ERROR" , t.message.toString())
                lookView.onGetSongsFailure(400 , "네트워크 오류가 발생하였습니다.")
            }
        })

    }

}