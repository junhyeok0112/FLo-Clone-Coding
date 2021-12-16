package com.example.flo.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getRetorfit() : Retrofit {
    return Retrofit.Builder()
        .baseUrl("http://13.125.121.202")          //retrofit 객체 생성
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}