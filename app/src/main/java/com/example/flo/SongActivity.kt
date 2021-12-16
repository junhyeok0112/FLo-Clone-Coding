package com.example.flo

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.example.flo.Data.Song
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit


class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding
    private lateinit var player:Player
    private var nowsong = Song()

    //미디어 플레이어 선언
    private var mediaPlayer : MediaPlayer? = null

    //Gson 선언
    private var gson : Gson = Gson()

    private var songs = ArrayList<Song>()
    private var nowPos = 0
    private lateinit var songDB: SongDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setImageView()
        setNowSong()
        initPlayList()
        initSong()        //제목 가수 플레이 타임 설정
        initListener()

    }

    fun setChangePlay(isPlaying: Boolean){
        player.isPlaying = isPlaying
        nowsong.isPlaying = isPlaying

        when(isPlaying){
            true->{     //플레이중으로 변경
                binding.songPauseIv.visibility = ImageView.VISIBLE
                binding.songPlayIv.visibility = ImageView.GONE

                mediaPlayer?.start()
            }
            false->{
                binding.songPauseIv.visibility = ImageView.GONE
                binding.songPlayIv.visibility = ImageView.VISIBLE

                //왠지 모르지만 start 한다음 pause해야함
                mediaPlayer?.start()
                mediaPlayer?.pause()
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

    fun setNowSong(){
        songDB = SongDatabase.getInstance(this)!!
        if(intent.hasExtra("songId")){
            nowsong = songDB.songDao().getSong(intent.getIntExtra("songId",1))
        }
    }

    fun initPlayList(){
        val albumId = nowsong.albumIdx
        songs.clear() //이전 앨범에 있는 노래 비움
        songs = songDB.songDao().getSongsInAlbum(albumId) as ArrayList
        Log.d("Song","SongList : ${songs.toString()}")
    }

    //여기서 문제가 생긴것같은데 ?
    fun initSong(){
        //MainActivity에서 받은 onPause 에서 저장한 SharedPreference 저장

        nowPos = getPlaySongPosition(nowsong.id)

        //전에 계속 진행 중이였으면 플레잉으로 셋
        startPlayer()
        setPlayer(nowsong)                    //실행할 노래 넣음

    }

    fun getPlaySongPosition(songId: Int) : Int{               //주어진 노래가 앨범에서 몇번째인지 셋팅
        for (i in 0 until songs.size){                        //리스트에서 해당하는 노래의 nowPos(몇번째인지 추출)
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    fun setPlayer(song: Song) {
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        Log.d("setPlayer","${song}")
        binding.songTitleTv.text = song.title
        binding.songSinggerTv.text = song.singger
        binding.songStartTv.text =
            String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTv.text =
            String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songSsumnailIv.setImageResource(song.coverImg!!)
        binding.songSeekbarSb.progress = (song.second * 1000 / song.playTime)
        Log.d("setPlayer" , "${song.second}")
        setChangePlay(song.isPlaying)

        if (song.isLike) {
            binding.songLikeIv.visibility = View.GONE
            binding.songLikeOnIv.visibility = View.VISIBLE
        } else {
            binding.songLikeIv.visibility = View.VISIBLE
            binding.songLikeOnIv.visibility = View.GONE
        }

        mediaPlayer = MediaPlayer.create(this, music)
        //노래 실행시키는 코드 추가
        binding.songSeekbarSb.max = mediaPlayer?.duration!!
        binding.songSeekbarSb.progress = song.second * 1000
        mediaPlayer?.seekTo(song.second * 1000)      //노래 끝나면 해제해줘야함
        setChangePlay(song.isPlaying)

    }

    fun setImageView(){
        binding.songSsumnailIv.background = resources.getDrawable(R.drawable.round_temp , null)
        binding.songSsumnailIv.clipToOutline = true
    }

    fun startPlayer(){
        player = Player(nowsong.playTime, nowsong.isPlaying)       //쓰레드 생성
        player.start()          //쓰레드 시작
    }

    //Thread 클래스 만들기
    inner class Player(private val playTime: Int , var isPlaying: Boolean ) : Thread(){
        var timeFormat = SimpleDateFormat("mm:ss")
        var second  = nowsong.second

        override fun run() {    //이 안에 코드가 start되면 시작
            try{                //interruput 위해서 try ~ catch 사용
                Log.d("Song" , "지금 시간 : ${second}")
                while (true){
                    if(second >= playTime){       //만약 끝까지 다 찼으면 멈춰
                        songDB.songDao().updateSecondById(0,nowsong.id)
                        break
                    }
                    if(isPlaying){
                        sleep(1000)
                        //Log.d("Song", "쓰레드 진행중")
                        second++
                        runOnUiThread {
                            binding.songStartTv.text = timeFormat.format(second * 1000)
                            binding.songSeekbarSb.progress = (second * 1000)
                        }
                    }
                }
            }catch (e : InterruptedException){
                Log.d("interruput" , "쓰레드가 종료되었습니다")
            }
        }
    }

    fun initListener(){
        //뒤로 가기시 현재 설정되어 있는 플레이 or pause main에 넘겨줌
        binding.songBackIv.setOnClickListener {
            nowsong.second = (binding.songSeekbarSb.progress) /1000
            songDB.songDao().updateSecondById(nowsong.second , nowsong.id)
            songDB.songDao().updateIsPlayingById(nowsong.isPlaying , nowsong.id)
            //intent 값이 잘 안 넘어가는 버그 있음
            val sharedPreferences = getSharedPreferences("song" , MODE_PRIVATE)
            val editor = sharedPreferences.edit()                   //sharedPreferences를 조작할 때 사용 -> 저장 등 할 때 사용

            editor.putInt("songId",nowsong.id)                //ID로 정보 전달 , Activity 전환되면서 현재 노래의 id 전달
            editor.apply()

            player.interrupt()
            mediaPlayer?.release()
            mediaPlayer = null

            finish()
        }


        //play , pause 바꾸기
        //내부 클래스의 변수에 접근가능
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
            setLike(nowsong.isLike)             //isLike 갱신
            showCustomToast("좋아요 한 곡에 담겼습니다.")
        }
        binding.songLikeOnIv.setOnClickListener {
            setChangeLike(it)
            setLike(nowsong.isLike)
            showCustomToast("좋아요 한 곡이 취소되었습니다.")
        }
        binding.songUnlikeIv.setOnClickListener {
            setChangeLike(it)
        }
        binding.songUnlikeOnIv.setOnClickListener {
            setChangeLike(it)
        }

        binding.songPreviousIv.setOnClickListener {
            moveSong(-1)
        }

        binding.songNextIv.setOnClickListener {
            moveSong(1)
        }

        //SeekBar 변화시 적용
        binding.songSeekbarSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mediaPlayer?.seekTo(progress)
                player.second = progress / 1000             //Player에 변경된 초도 갱신해주어야지 다음곡으로 넘어감
                nowsong.second = progress / 1000            //지금 재생되는 노래의 시간초 계산
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //터치했을 때
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //터치 끝났을 때
            }
        })
    }

    private fun moveSong(direct : Int){
        if (nowPos + direct < 0){
            Toast.makeText(this,"first song", Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direct >= songs.size){
            Toast.makeText(this,"last song", Toast.LENGTH_SHORT).show()
            return
        }
        songDB.songDao().updateSecondById(0,nowsong.id)
        nowPos += direct
        player.interrupt()
        startPlayer()
        mediaPlayer?.release() // 미디어플레이어가 가지고 있던 리소스를 해방
        mediaPlayer = null // 미디어플레이어 해제
        nowsong = songs[nowPos]
        setPlayer(nowsong)

    }

    private fun setLike(isLike: Boolean){
        nowsong.isLike = !isLike
        songDB.songDao().updateIsById(!isLike , nowsong.id)
    }

    fun showCustomToast(msg : String){
        val view = layoutInflater.inflate(R.layout.toast_like, null)
        var textView : TextView = view.findViewById(R.id.toast_msg_tv)
        textView.text = msg
        var toast = Toast(this)
        toast.view = view
        toast.show()
    }

    override fun onPause() {         //액티비티가 포커스 잃으면 노래 중지
        super.onPause()
        mediaPlayer?.pause()        //미디어 플레이어 중지
        nowsong.second = (binding.songSeekbarSb.progress) / 1000            //확인해보기
        nowsong.isPlaying = false

        setChangePlay(false)

        //앱이 종료되었다가 다시 시작하면 종료된 시점부터 사용하기 위해 종료 시점을 sharedPreferences 로 저장
        val sharedPreferences = getSharedPreferences("song" , MODE_PRIVATE)
        val editor = sharedPreferences.edit()                   //sharedPreferences를 조작할 때 사용 -> 저장 등 할 때 사용

        editor.putInt("songId",nowsong.id)                //ID로 정보 전달
        editor.apply()

    }

    override fun onDestroy() {
        super.onDestroy()
        player.interrupt()
        mediaPlayer?.release()      //미디어 플레이어 리소스 해제(음악 해제) -> 메모리 절약을 위해 해제해주어야함
        mediaPlayer = null
    }

}