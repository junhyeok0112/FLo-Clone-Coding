package com.example.flo.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.flo.MainActivity
import com.example.flo.databinding.ActivitySplashBinding
import com.example.flo.getJwt
import com.example.flo.login.LoginActivity
import com.example.flo.retrofit.Auth
import com.example.flo.retrofit.AuthService

class SplashActivity : AppCompatActivity() ,SplashView{

    lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            val jwt = getJwt(this)              //이 jwt를 담아서 보내야함
            val authService = AuthService()
            authService.setSplashView(this)

            authService.autoLogin(jwt)

        },2000)



    }

    override fun onAutoLoginSuccess() {
        //로그인 성공했을 때
        Log.d("AUTOLOGIN" , "로그인 성공")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }

    override fun onAutoLoginFailure(code: Int, message: String) {
        Log.d("AUTOLOGIN" , "로그인 실패")
        when(code){
            2002,2001->{
                Log.d("AUTOLOGIN-ERROR" , message)
            }
        }
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}