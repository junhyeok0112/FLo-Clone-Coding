package com.example.flo.login

//액티비티와 서비스 클래스 연결시켜놓은 인터페이스
interface SignUpView {
    fun onSginUpLoading()
    fun onSignUpSuccess()
    fun onSignUpFailure(code:Int , message : String)
}