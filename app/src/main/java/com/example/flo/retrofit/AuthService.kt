package com.example.flo.retrofit

import android.util.Log
import com.example.flo.User
import com.example.flo.login.LoginView
import com.example.flo.login.SignUpView
import com.example.flo.splash.SplashView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class AuthService { //레트로핏이 호출하고 응답하는걸 관리하는 클래스
    private lateinit var signUpView : SignUpView
    private lateinit var loginView: LoginView
    private lateinit var splashView : SplashView

    fun setSignUpView(signUpView: SignUpView){ //연결시켜줄 메소드
        this.signUpView = signUpView
    }

    fun setLoginView(loginView: LoginView){
        this.loginView = loginView
    }

    fun setSplashView(splashView: SplashView){
        this.splashView = splashView
    }

    //API 호출하고 관리하는 메서드 호출 , 이 안에서 직접적으로 API 호출
    fun signUp(user:User){
        val authService = getRetorfit().create(AuthRetrofitInterface::class.java)

        signUpView.onSginUpLoading()            //호출 전에 로딩시켜서 기다리게함

        authService.signUp(user).enqueue(object : Callback<AuthResponse> {

            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUPACT/API-RESPONSE" , response.toString())       //네트워크에서 보내준 응답값

                //서버가 보내준 데이터 파싱하려면 response.body()사용
                val resp = response.body()!!

                when(resp.code){
                    1000->signUpView.onSignUpSuccess()      //여기서 받아서 액티비티와 연결된 인터페이스 메서드를 이용해서 처리
                    else -> signUpView.onSignUpFailure(resp.code , resp.message)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUPACT/API-ERROR" , t.message.toString())
                signUpView.onSignUpFailure(400 , "네트워크 오류가 발생하였습니다.")
            }
        })

    }

    fun login(user:User){
        val authService = getRetorfit().create(AuthRetrofitInterface::class.java)

        loginView.onLoginLoading()            //호출 전에 로딩시켜서 기다리게함

        authService.login(user).enqueue(object : Callback<AuthResponse> {

            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("LOGINACT/API-RESPONSE" , response.toString())       //네트워크에서 보내준 응답값

                //서버가 보내준 데이터 파싱하려면 response.body()사용
                val resp = response.body()!!
                Log.d("LOGINACT/API-RESPONSE" , resp.code.toString())
                when(resp.code){
                    1000 -> loginView.onLoginSuccess(resp.result!!)      //여기서 받아서 액티비티와 연결된 인터페이스 메서드를 이용해서 처리
                    else -> loginView.onLoginFailure(resp.code , resp.message)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("LOGINACT/API-ERROR" , t.message.toString())
                signUpView.onSignUpFailure(400 , "네트워크 오류가 발생하였습니다.")
            }
        })

    }

    fun autoLogin(jwt:String){
        val authService = getRetorfit().create(AuthRetrofitInterface::class.java)


        authService.autoLogin(jwt).enqueue(object : Callback<AuthResponse> {

            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("AUTOLOGIN-RESPONSE" , response.toString())       //네트워크에서 보내준 응답값

                //서버가 보내준 데이터 파싱하려면 response.body()사용
                val resp = response.body()!!
                Log.d("AUTOLOGIN-RESPONSE" , resp.code.toString())
                when(resp.code){
                    1000 -> splashView.onAutoLoginSuccess()      //여기서 받아서 액티비티와 연결된 인터페이스 메서드를 이용해서 처리
                    else -> splashView.onAutoLoginFailure(resp.code , resp.message)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("AUTOLOGIN/API-ERROR" , t.message.toString())
                signUpView.onSignUpFailure(400 , "네트워크 오류가 발생하였습니다.")
            }
        })

    }


}