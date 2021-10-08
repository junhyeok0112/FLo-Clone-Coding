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

        //like 버튼
        binding.songLikeIv.setOnClickListener {
            setChangeLike(it)
        }
        binding.songLikeOnIv.setOnClickListener {
            setChangeLike(it)
        }
        binding.songUnlikeIv.setOnClickListener {
            setChangeLike(it)
        }
        binding.songUnlikeOnIv.setOnClickListener {
            setChangeLike(it)
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

    fun setChangeLike(view :View){
        when(view.id){
            R.id.song_like_iv ->{
                binding.songLikeIv.visibility = View.GONE
                binding.songLikeOnIv.visibility = View.VISIBLE
            }
            R.id.song_like_on_iv ->{
                binding.songLikeOnIv.visibility = View.GONE
                binding.songLikeIv.visibility = View.VISIBLE
            }
            R.id.song_unlike_iv ->{
                binding.songUnlikeIv.visibility = View.GONE
                binding.songUnlikeOnIv.visibility = View.VISIBLE
            }
            R.id.song_unlike_on_iv->{
                binding.songUnlikeOnIv.visibility = View.GONE
                binding.songUnlikeIv.visibility = View.VISIBLE
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
        if(intent.hasExtra("isPlaying")){
            val isPlaying = intent.getBooleanExtra("isPlaying" , true)
            //넘어온 true ,false값을 가지고 setChangePlay 호출 -> 이 때 매개변수가 false 면 Play로 바꾸고 있게 구현한 함수이므로
            //isPlaying  이 true면 즉 Play되고 있으면 false로 바꿔서 넘겨줘야함
            setChangePlay(!isPlaying)

        }
    }

    fun setImageView(){
        binding.songSsumnailIv.background = resources.getDrawable(R.drawable.round_temp , null)
        binding.songSsumnailIv.clipToOutline = true
    }
}