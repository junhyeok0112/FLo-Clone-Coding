package com.example.flo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setImageView()
        setTitleAndSingger()        //제목 가수 설정



        //뒤로 가기시 현재 설정되어 있는 플레이 or pause main에 넘겨줌
        binding.songBackIv.setOnClickListener {
            var isPlaying = true
            if(binding.songPlayIv.visibility == View.GONE){ //멈춤상태면
                isPlaying = false
            } else{
                isPlaying = true
            }

            //현재 플레이 상태 돌려보내기
            val intent = Intent()
            intent.putExtra("isPlaying" , isPlaying)
            setResult(RESULT_OK , intent)
            finish()
        }

        //play , pause 바꾸기
        binding.songPlayIv.setOnClickListener {
            setChangePlay(true)
        }

        binding.songPauseIv.setOnClickListener {
            setChangePlay(false)
        }

        //반복재생 이미지 바꾸기
        binding.songRepeatIv.setOnClickListener {
            setChangeRepeat(it)
        }

        binding.songRepeatOnIv.setOnClickListener {
            setChangeRepeat(it)
        }

        binding.songRepeatOn1Iv.setOnClickListener {
            setChangeRepeat(it)
        }

        binding.songRepeatPlaylistIv.setOnClickListener {
            setChangeRepeat(it)
        }

        //랜덤재생 껏다 키기
        binding.songRandomIv.setOnClickListener {
            setChangeRandom(false)
        }

        binding.songRandomOnIv.setOnClickListener {
            setChangeRandom(true)
        }


    }

    fun setChangePlay(isPlaying: Boolean){
        when(isPlaying){
            true->{
                binding.songPauseIv.visibility = ImageView.VISIBLE
                binding.songPlayIv.visibility = ImageView.GONE
            }
            false->{
                binding.songPauseIv.visibility = ImageView.GONE
                binding.songPlayIv.visibility = ImageView.VISIBLE
            }
        }
    }

    fun setChangeRandom(isOn:Boolean){      //반복재생 버튼 변경 false면 켜라  , true면 꺼라
        when(isOn){
            true->{
                binding.songRandomOnIv.visibility = View.GONE
                binding.songRandomIv.visibility = View.VISIBLE
            }
            false->{
                binding.songRandomOnIv.visibility = View.VISIBLE
                binding.songRandomIv.visibility = View.GONE
            }
        }
    }

    fun setChangeRepeat(view : View){       //반복재생 버튼 이미지 변경
        when(view.id){
            R.id.song_repeat_iv ->{
                binding.songRepeatIv.visibility = View.GONE
                binding.songRepeatOnIv.visibility = View.VISIBLE
            }
            R.id.song_repeat_on_iv->{
                binding.songRepeatOnIv.visibility = View.GONE
                binding.songRepeatOn1Iv.visibility = View.VISIBLE
            }
            R.id.song_repeat_on1_iv->{
                binding.songRepeatOn1Iv.visibility = View.GONE
                binding.songRepeatPlaylistIv.visibility = View.VISIBLE
            }
            R.id.song_repeat_playlist_iv->{
                binding.songRepeatPlaylistIv.visibility = View.GONE
                binding.songRepeatIv.visibility = View.VISIBLE
            }
        }
    }

    fun setTitleAndSingger(){
        if(intent.hasExtra("title") && intent.hasExtra("singger")){
            val title = intent.getStringExtra("title")
            val singger = intent.getStringExtra("singger")
            binding.songTitleTv.text = title
            binding.songSinggerTv.text = singger
        }
    }

    fun setImageView(){
        binding.songSsumnailIv.background = resources.getDrawable(R.drawable.round_temp , null)
        binding.songSsumnailIv.clipToOutline = true
    }
}