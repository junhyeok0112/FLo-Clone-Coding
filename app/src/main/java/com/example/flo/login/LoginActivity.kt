package com.example.flo.login

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.*
import com.example.flo.databinding.ActivityLoginBinding
import com.example.flo.retrofit.Auth
import com.example.flo.retrofit.AuthService

class LoginActivity:AppCompatActivity() , LoginView {

    lateinit var binding:ActivityLoginBinding
    var isOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.loginSignupTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.loginLoginBtn.setOnClickListener {
            login()
        }

        binding.loginPasswordOffIv.setOnClickListener {
            if(isOn){
                binding.loginPasswordOffIv.setImageResource(R.drawable.btn_input_password_off)
                binding.loginPasswordEt.inputType = InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_PASSWORD
                isOn = false
            }else{
                binding.loginPasswordOffIv.setImageResource(R.drawable.btn_input_password)
                binding.loginPasswordEt.inputType = InputType.TYPE_CLASS_TEXT
                isOn = true
            }
        }

    }

    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


    private fun login(){
        if(binding.loginIdInputEt.text.toString().isEmpty() || binding.loginEmailInputEt.text.toString().isEmpty()){
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if(binding.loginIdInputEt.text.toString().isEmpty()){
            Toast.makeText(this,"비밀번호를 입력해주세요." , Toast.LENGTH_SHORT).show()
            return
        }

        val authService = AuthService()
        authService.setLoginView(this)

        authService.login(getUser())

    }

    private fun getUser() : User {
        val email = binding.loginIdInputEt.text.toString() + "@" + binding.loginEmailInputEt.text.toString()
        val password = binding.loginPasswordEt.text.toString()

        return User(email, password ,"")
    }

    override fun onLoginLoading() {
        binding.loginLoadingPb.visibility = View.VISIBLE
    }

    override fun onLoginSuccess(auth:Auth) {
        binding.loginLoadingPb.visibility = View.GONE
        Log.d("LOGINACT/GET_USER", "userid : ${auth.userIdx} , $auth")
            //발급 받은 jwt를 저장해주는 함수
        saveJwt(this, auth.jwt)               //서버에서 받아온 jwt 저장     -> 로그인 후 이거 이용
        saveUserIdx(this,auth.userIdx)          //충돌이 안나게 하기 위해서 UserIndex로 저장

        startMainActivity()

    }

    override fun onLoginFailure(code:Int , message:String) {
        binding.loginLoadingPb.visibility = View.GONE

        when(code){
            2015,2019,3014->{
                binding.loginErrorTv.visibility = View.VISIBLE
                binding.loginErrorTv.text = message
            }
        }
    }
    
}