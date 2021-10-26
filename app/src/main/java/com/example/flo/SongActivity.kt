package com.example.flo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import com.example.flo.databinding.ActivitySongBinding


class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding
    private lateinit var player:Player
    private val song = Song()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setImageView()
        initSong()        //제목 가수 플레이 타임 설정

        player = Player(song.playTime, song.isPlaying)       //쓰레드 생성
        player.initSecond()
        player.start()          //쓰레드 시작

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
        //내부 클래스의 변수에 접근가능
        binding.songPlayIv.setOnClickListener {
            player.isPlaying = true         //쓰레드에 있는 isPlaying도 바꿔줘야함
            setChangePlay(true)
        }

        binding.songPauseIv.setOnClickListener {
            player.isPlaying = false
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


        //SeekBar 변화시 적용
        binding.songSeekbarSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var second = binding.songSeekbarSb.progress * song.playTime / 1000
                player.setSecond(second)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //터치했을 때
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //터치 끝났을 때
            }
        })
    }

    fun setChangePlay(isPlaying: Boolean){
        when(isPlaying){
            true->{     //플레이중으로 변경
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

    fun initSong(){
        if(intent.hasExtra("title") && intent.hasExtra("singger") && intent.hasExtra("playTime")){
            song.title = intent.getStringExtra("title")!!
            song.singger = intent.getStringExtra("singger")!!
            song.playTime = intent.getIntExtra("playTime",0)

            binding.songEndTv.text = String.format("%02d:%02d" , song.playTime/60, song.playTime % 60)
            binding.songTitleTv.text = song.title
            binding.songSinggerTv.text = song.singger
        }
        if(intent.hasExtra("isPlaying")){
            song.isPlaying = intent.getBooleanExtra("isPlaying" , true)
            //넘어온 true ,false값을 가지고 setChangePlay 호출 -> 이 때 매개변수가 false 면 Play로 바꾸고 있게 구현한 함수이므로
            //isPlaying  이 true면 즉 Play되고 있으면 false로 바꿔서 넘겨줘야함
            setChangePlay(song.isPlaying)
        }
        if(intent.hasExtra("progress")){        //넘어온 양의 progress 받기
            binding.songSeekbarSb.progress = intent.getIntExtra("progress" , 0)
        }
    }

    fun setImageView(){
        binding.songSsumnailIv.background = resources.getDrawable(R.drawable.round_temp , null)
        binding.songSsumnailIv.clipToOutline = true
    }

    //Thread 클래스 만들기
    inner class Player(private val playTime: Int , var isPlaying: Boolean) : Thread(){
        private var second = 0      //초기 시간

        override fun run() {    //이 안에 코드가 start되면 시작
            try{                //interruput 위해서 try ~ catch 사용
                while (true){
                    if(second >= playTime){
                        if(binding.songRepeatOn1Iv.visibility == View.VISIBLE){
                            second = 0
                        } else{
                            break
                        }
                    }
                    if(isPlaying){
                        sleep(1000)
                        second++
                        runOnUiThread {
                            binding.songStartTv.text = String.format("%02d:%02d" , second/60 , second %60)
                            binding.songSeekbarSb.progress = second * 1000 / playTime
                        }
                    }
                }
            }catch (e : InterruptedException){
                Log.d("interruput" , "쓰레드가 종료되었습니다")
            }
        }

        fun setSecond(tempSecond:Int){
            second = tempSecond
            binding.songStartTv.text = String.format("%02d:%02d" , second/60 , second %60)
        }
        fun initSecond(){       //Main에서 넘어온 만큼의 값으로 셋팅
            second = binding.songSeekbarSb.progress * song.playTime / 1000
            binding.songStartTv.text = String.format("%02d:%02d" , second/60 , second %60)
        }
    }

    override fun onDestroy() {
        player.interrupt()
        super.onDestroy()
    }
}