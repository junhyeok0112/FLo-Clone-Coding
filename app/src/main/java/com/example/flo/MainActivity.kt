package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.homefragment.HomeFragment
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.lockerfragment.LockerFragment

//import com.example.flo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var activityLaucher : ActivityResultLauncher<Intent>
    lateinit var player:Player
    var playTime = 215
    var isPlaying: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
        //임시로 선언한 playTime 과 isPlaying 셋팅
        if(binding.mainMiniplayerBtn.visibility == View.VISIBLE){   //멈춰있을 경우
            isPlaying = false
        }else{
            isPlaying = true
        }

        player = Player(playTime , isPlaying)
        player.start()

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
            var isPlaying = false    //플레이 중일떄
            if(binding.mainMiniplayerBtn.visibility == View.GONE){ //멈춰있을떄
                isPlaying = true
            }
            song.isPlaying = isPlaying
            song.playTime = 215         //더미 데이터로 만듬
            val intent = Intent(this,SongActivity::class.java)
            intent.putExtra("title" , song.title)
            intent.putExtra("singger" , song.singger)
            intent.putExtra("isPlaying" , song.isPlaying)
            intent.putExtra("playTime" , song.playTime)
            intent.putExtra("progress" , binding.mainSeekBarSb.progress)
            activityLaucher.launch(intent)      //startActivityForResult 대신 실행
        }

        //미니 플레이어 버튼 눌렀을 시 변경
        binding.mainMiniplayerBtn.setOnClickListener {
            player.isPlaying = true
            setChangePlay(true)
        }

        binding.mainMiniplayerPause.setOnClickListener {
            player.isPlaying = false
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
            true->{ //플레이 중이게 해라
                binding.mainMiniplayerPause.visibility = ImageView.VISIBLE
                binding.mainMiniplayerBtn.visibility = ImageView.GONE
            }
            false->{
                binding.mainMiniplayerPause.visibility = ImageView.GONE
                binding.mainMiniplayerBtn.visibility = ImageView.VISIBLE
            }
        }
    }

    //Thread 클래스 만들기
    inner class Player(private val playTime: Int , var isPlaying: Boolean) : Thread(){
        private var second = 0      //초기 시간
        override fun run() {    //이 안에 코드가 start되면 시작
            while (true){
                if(second >= playTime){     //시간 다 되면 멈추기
                   break
                }
                if(isPlaying){
                    sleep(1000)
                    second++
                    runOnUiThread {
                        binding.mainSeekBarSb.progress = second * 1000 / playTime
                    }
                }
            }
        }

        fun setSecond(tempSecond:Int){
            second = tempSecond
        }
    }

}

