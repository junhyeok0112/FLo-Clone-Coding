package com.example.flo

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

fun saveJwt(context:Context ,jwt:String){
    val spf = context.getSharedPreferences("auth" , AppCompatActivity.MODE_PRIVATE)
    val editor = spf.edit()

    editor.putString("jwt",jwt)
    editor.apply()
}

fun getJwt(context:Context) : String{
    val spf = context.getSharedPreferences("auth" ,AppCompatActivity.MODE_PRIVATE)

    Log.d("GETJWT" , "${spf.getString("jwt","")}")
    return spf.getString("jwt","")!!.toString()
}


fun saveUserIdx(context: Context , userIdx:Int){
    val spf = context.getSharedPreferences("auth" , AppCompatActivity.MODE_PRIVATE)
    val editor = spf.edit()

    editor.putInt("userIdx" , userIdx)
    editor.apply()
}

fun getUserIdx(context: Context) : Int{
    val spf = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

    return spf.getInt("userIdx" , 0)
}