package com.example.flo.login

import com.example.flo.retrofit.Auth

interface LoginView {
    fun onLoginLoading()
    fun onLoginSuccess(auth: Auth)
    fun onLoginFailure(code:Int , message:String)
}