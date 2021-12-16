package com.example.flo.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.flo.User
import com.example.flo.databinding.ActivitySignUpBinding
import com.example.flo.retrofit.AuthService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUpActivity : AppCompatActivity() , SignUpView {

    lateinit var binding : ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupBtn.setOnClickListener {
            signUp()
            //finish()
        }

    }

    private fun getUser(): User {
        val email = binding.signupIdInputEt.text.toString() + "@" + binding.signupEmailInputEt.text.toString()
        val pwd = binding.signupPasswordEt.text.toString()
        val name = binding.signupInputNameEt.text.toString()

        return  User(email ,pwd,name)
    }

    private fun signUp(){
        if(binding.signupIdInputEt.text.toString().isEmpty() || binding.signupEmailInputEt.text.toString().isEmpty()){
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if(binding.signupInputNameEt.text.toString().isEmpty()){
            Toast.makeText(this, "이름을 입력해 주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if(binding.signupPasswordEt.text.toString() != binding.signupPasswordChkEt.text.toString()){
            Toast.makeText(this,"비밀번호가 일치하지 않습니다." , Toast.LENGTH_SHORT).show()
            return
        }

        val retrofit = Retrofit.Builder().baseUrl("http://13.125.121.202")          //retrofit 객체 생성
            .addConverterFactory(GsonConverterFactory.create()).build()

        val authService = AuthService()
        authService.setSignUpView(this)
        //비동기로 처리됨 따라서 응답이 올때가지 기다리는게 아님
        authService.signUp(getUser())

    }

    override fun onSginUpLoading() {
        binding.signupLoadingPb.visibility = View.VISIBLE
    }

    override fun onSignUpSuccess() {
        binding.signupLoadingPb.visibility = View.GONE

        finish()
    }

    override fun onSignUpFailure(code: Int, message: String) {
        binding.signupLoadingPb.visibility = View.GONE

        when(code){
            2016,2017 -> {
                binding.signupErrorTv.visibility = View.VISIBLE
                binding.signupErrorTv.text = message
            }
        }
    }

}