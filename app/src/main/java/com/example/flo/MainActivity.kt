package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding
//import com.example.flo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var activityLaucher : ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()


        //콜백 메서드 설정 -> onActivityResult와 같은역할
        activityLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK) {
                //default값 줘야함
                val isPlaying = result.data?.getBooleanExtra("isPlaying" , true)
                if(isPlaying == true){
                    binding.mainMiniplayerBtn.visibility = View.VISIBLE
                    binding.mainMiniplayerPause.visibility = View.GONE
                }
                else{
                    binding.mainMiniplayerBtn.visibility = View.GONE
                    binding.mainMiniplayerPause.visibility = View.VISIBLE
                }
            }
        }



        //미니 플레이어 클릭시 실행
        binding.mainPlayerLayout.setOnClickListener {
            var title = binding.mainTitleTv.text.toString()
            var singger = binding.mainSinggerTv.text.toString()
            var song = Song(title,singger)
            val intent = Intent(this,SongActivity::class.java)
            intent.putExtra("title" , song.title)
            intent.putExtra("singger" , song.singger)
            activityLaucher.launch(intent)      //startActivityForResult 대신 실행
        }

        //미니 플레이어 버튼 눌렀을 시 변경
        binding.mainMiniplayerBtn.setOnClickListener {
            setChangePlay(true)
        }

        binding.mainMiniplayerPause.setOnClickListener {
            setChangePlay(false)
        }


        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }


    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

    }

    fun setChangePlay(isPlaying: Boolean){
        when(isPlaying){
            true->{
                binding.mainMiniplayerPause.visibility = ImageView.VISIBLE
                binding.mainMiniplayerBtn.visibility = ImageView.GONE
            }
            false->{
                binding.mainMiniplayerPause.visibility = ImageView.GONE
                binding.mainMiniplayerBtn.visibility = ImageView.VISIBLE
            }
        }
    }

}

