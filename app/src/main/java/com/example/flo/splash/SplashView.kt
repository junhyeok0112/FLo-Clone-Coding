package com.example.flo.splash

import com.example.flo.retrofit.Auth

interface SplashView {
    fun onAutoLoginSuccess()
    fun onAutoLoginFailure(code:Int, message:String)
}