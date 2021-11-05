package com.example.flo

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson
import java.lang.Exception
import java.text.SimpleDateFormat


class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding
    private lateinit var player:Player
    private var song = Song()
    
    //미디어 플레이어 선언
    private var mediaPlayer : MediaPlayer? = null

    //Gson 선언
    private var gson : Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setImageView()
        initSong()        //제목 가수 플레이 타임 설정

        player = Player(song.playTime, song.isPlaying)       //쓰레드 생성
        player.start()          //쓰레드 시작
        Log.d("Song","Thread 시작")

        //뒤로 가기시 현재 설정되어 있는 플레이 or pause main에 넘겨줌
        binding.songBackIv.setOnClickListener {
            song.currentPosition = mediaPlayer?.currentPosition!!
            val intent = Intent()
            val songToJson = gson.toJson(song)
            intent.putExtra("song",songToJson)
            setResult(RESULT_OK , intent)
            finish()
        }


        //play , pause 바꾸기
        //내부 클래스의 변수에 접근가능
        binding.songPlayIv.setOnClickListener {
            player.isPlaying = true         //쓰레드에 있는 isPlaying도 바꿔줘야
            mediaPlayer?.start()            //플레이 버튼 눌렀을 때 음악 재생
            song.isPlaying = true           //현재 노래 재생상태도 저장
            setChangePlay(true)
        }

        binding.songPauseIv.setOnClickListener {
            player.isPlaying = false
            mediaPlayer?.pause()
            song.isPlaying = false
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
                mediaPlayer?.seekTo(progress)
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
        if(intent.hasExtra("song")){
            song = gson.fromJson(intent.getStringExtra("song"),Song::class.java)
            Log.d("song","SongActivity에서 song : ${song}")
            var timeFormat = SimpleDateFormat("mm:ss")

            binding.songStartTv.text= timeFormat.format(song.currentPosition)
            binding.songEndTv.text = timeFormat.format(song.playTime)
            binding.songTitleTv.text = song.title
            binding.songSinggerTv.text = song.singger

            setChangePlay(song.isPlaying)

            val music = resources.getIdentifier(song.music , "raw" , this.packageName)      //리소스 이름을 가지고 리소스 반환받기
            mediaPlayer = MediaPlayer.create(this , music)
            binding.songSeekbarSb.max = mediaPlayer?.duration!!                             //SeekBar의 최대치를 Music과 맞춤
            binding.songSeekbarSb.progress = song.currentPosition                           //받아온 정보로 SeekBar 셋팅
            mediaPlayer?.seekTo(song.currentPosition)                                        //진행 되었던 부분까지 셋팅
            Log.d("song","music create 실행")
            if(song.isPlaying){                     //Main에서 멈췄던 위치 받아오고 만약 Play상태면 계속 실행
                mediaPlayer?.start()
            } else{
                mediaPlayer?.start()
                mediaPlayer?.pause()
            }
        }
    }

    fun setImageView(){
        binding.songSsumnailIv.background = resources.getDrawable(R.drawable.round_temp , null)
        binding.songSsumnailIv.clipToOutline = true
    }

    //Thread 클래스 만들기
    inner class Player(private val playTime: Int , var isPlaying: Boolean ) : Thread(){
        var timeFormat = SimpleDateFormat("mm:ss")
        var second = song.currentPosition * 1000
        override fun run() {    //이 안에 코드가 start되면 시작
            try{                //interruput 위해서 try ~ catch 사용
                while (true){
                    if(song.currentPosition >= playTime){       //만약 끝까지 다 찼으면 초기화
                        if(binding.songRepeatOn1Iv.visibility == View.VISIBLE){
                            binding.songSeekbarSb.progress = 0
                            mediaPlayer?.pause()
                            mediaPlayer?.seekTo(0)       //처음으로 돌아가
                        } else{
                            Log.d("Song","Thread 탈출")
                            break
                        }
                    }
                    if(isPlaying){
                        sleep(1000)
                        //Log.d("Song", "쓰레드 진행중")
                        second++
                        runOnUiThread {
                            binding.songStartTv.text = timeFormat.format(mediaPlayer?.currentPosition)
                            binding.songSeekbarSb.progress = mediaPlayer?.currentPosition!!
                        }
                    }
                }
            }catch (e : InterruptedException){
                Log.d("interruput" , "쓰레드가 종료되었습니다")
            }
        }

    }

    override fun onPause() {         //액티비티가 포커스 잃으면 노래 중지
        super.onPause()
        mediaPlayer?.pause()        //미디어 플레이어 중지
        player.isPlaying = false    //쓰레드 중지
        song.isPlaying = false      //중지되었으니까 멈춤
        song.currentPosition = mediaPlayer?.currentPosition!!
        setChangePlay(false)

        //앱이 종료되었다가 다시 시작하면 종료된 시점부터 사용하기 위해 종료 시점을 sharedPreferences 로 저장
        val sharedPreferences = getSharedPreferences("song" , MODE_PRIVATE)
        val editor = sharedPreferences.edit()                   //sharedPreferences를 조작할 때 사용 -> 저장 등 할 때 사용
        val songToJson = gson.toJson(song)                     //song 데이터 객체를 Json 형태로 저장해줄것임
        editor.putString("song", songToJson)                    //Gson 이 객체를 String 형식으로 한줄로 정렬
        editor.apply()

    }

    override fun onStart() {
        super.onStart()

    }
    override fun onDestroy() {
        super.onDestroy()
        player.interrupt()
        mediaPlayer?.release()      //미디어 플레이어 리소스 해제(음악 해제) -> 메모리 절약을 위해 해제해주어야함
        mediaPlayer = null
    }

}