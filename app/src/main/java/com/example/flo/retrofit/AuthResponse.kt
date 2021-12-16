package com.example.flo.retrofit

data class Auth(val userIdx:Int ,val jwt:String)

data class AuthResponse(
    val isSuccess : Boolean,
    val code : Int,
    val message : String,
    val result:Auth?            //공통되게 사용하기 위해 Null도 가능하게 처리
)
