package com.example.flo

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.MediaController
import android.widget.SeekBar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.homefragment.HomeFragment
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.lockerfragment.LockerFragment
import com.google.gson.Gson
import java.text.SimpleDateFormat
import kotlin.concurrent.thread

//import com.example.flo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var activityLaucher : ActivityResultLauncher<Intent>
    lateinit var player:Player
    var isPlaying = false       //Main에서 MiniPlayer가 실행되고있는지 아닌지 체크
    //Gson 선언
    private var gson:Gson = Gson()
    private var song:Song = Song()
    private var mediaPlayer : MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()

        song = Song("라일락" , "아이유(IU)", "",215,false,"music_lilac",0)   //기본값 셋팅
        setMiniPlayer(song) //더미 기본값 셋팅
        player = Player(song.playTime , song.isPlaying )
        player.start()
        //mediaPlayer?.prepareAsync()


        //SeekBar 변화시 적용
        binding.mainSeekBarSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser){
                    mediaPlayer?.seekTo(progress)              //progress는 215까지 수인데 mediaPlayer에서 바꾸려면 1000을 곱해주어야함
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //터치했을 때
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //터치 끝났을 때
            }
        })

        //콜백 메서드 설정 -> onActivityResult와 같은역할
        activityLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK) {
                song = gson.fromJson(result.data?.getStringExtra("song"),Song::class.java)      //받아온 정보를 song 객체로변환
                player.isPlaying = song.isPlaying
                isPlaying = song.isPlaying
                setChangePlay(song.isPlaying)
                setMiniPlayer(song)
                Log.d("Main" , "Song에서 Main으로 돌아옴 : ${song}")
                if(song.isPlaying){                     //계속 플레이중일떄
                    mediaPlayer?.start()
                }else{
                    mediaPlayer?.start()        //start를 안해주면 오류가 남 -> 왜 ?
                    mediaPlayer?.pause()
                }
            }
        }


        //미니 플레이어 클릭시 실행
        binding.mainPlayerLayout.setOnClickListener {
            song.currentPosition= mediaPlayer!!.currentPosition
            val intent = Intent(this,SongActivity::class.java)
            val songToJson = gson.toJson(song)
            intent.putExtra("song",songToJson)
            activityLaucher.launch(intent)      //startActivityForResult 대신 실행
        }

        //미니 플레이어 버튼 눌렀을 시 변경
        binding.mainMiniplayerBtn.setOnClickListener {
            player.isPlaying = true
            isPlaying = true
            song.isPlaying = true
            mediaPlayer?.start()
            setChangePlay(true)
        }

        binding.mainMiniplayerPause.setOnClickListener {
            player.isPlaying = false
            isPlaying = false
            song.isPlaying = false
            mediaPlayer?.pause()
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


    fun setMiniPlayer(song : Song){     //넘어온 Song의 데이터 저장
        binding.mainTitleTv.text = song.title
        binding.mainSinggerTv.text = song.singger
        val music = resources.getIdentifier(song.music,  "raw",this.packageName )
        mediaPlayer = MediaPlayer.create(this,music)
        binding.mainSeekBarSb.max = mediaPlayer?.duration!!
        song.playTime = mediaPlayer?.duration!!              //총 재생시간 설정
        binding.mainSeekBarSb.progress = song.currentPosition
        mediaPlayer?.seekTo(song.currentPosition)
        if(song.isPlaying){
            binding.mainMiniplayerPause.visibility = View.VISIBLE
            binding.mainMiniplayerBtn.visibility = View.GONE
        } else{
            binding.mainMiniplayerPause.visibility = View.GONE
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
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
    inner class Player(private val playTime: Int , var isPlaying: Boolean ) : Thread(){
        var timeFormat = SimpleDateFormat("mm:ss")
        override fun run() {    //이 안에 코드가 start되면 시작
            try{
                while (true){
                    if(mediaPlayer?.currentPosition!! >= playTime){     //시간 다 되면 멈추기
                        break
                    }
                    if(isPlaying){
                        sleep(1)
                        runOnUiThread {
                            binding.mainSeekBarSb.progress = mediaPlayer?.currentPosition!!
                        }
                    }
                }
            } catch (e:InterruptedException){
                Log.d("Main Player" , "Main Player 쓰레드가 종료되었습니다")
            }
        }
    }

    override fun onStart() {        //SharedPreference에 저장된 값들 가져오는 역할 , UI 관련 함수 등 메소드 초기화
        super.onStart()
        Log.d("Main onStart" , "onStart실행")
        val sharedPreferences = getSharedPreferences("song" , MODE_PRIVATE)
        //값을 가져올 때는 editor 필요 없음
        val jsonSong = sharedPreferences.getString("song" , null)   //Song 데이터지만 Json 형태
        song = if(jsonSong == null){        //앱 처음 실행했을떄 SharedPrefereces없으므로 값 초기화
            Song("라일락" , "아이유(IU)", "",215,false,"music_lilac",0)
        } else{
            gson.fromJson(jsonSong , Song::class.java)
        }
        setMiniPlayer(song)
    }

    override fun onPause() {         //액티비티가 포커스 잃으면 노래 중지
        super.onPause()
        mediaPlayer?.pause()        //미디어 플레이어 중지
        player.isPlaying = false    //쓰레드 중지
        song.isPlaying = false      //중지되었으니까 멈춤
        song.currentPosition= mediaPlayer?.currentPosition!!        //노래의 멈춘부분
        setChangePlay(false)
        Log.d("main" , "Main의 onPause : ${song}")
        //앱이 종료되었다가 다시 시작하면 종료된 시점부터 사용하기 위해 종료 시점을 sharedPreferences 로 저

        val sharedPreferences = getSharedPreferences("song" , MODE_PRIVATE)
        val editor = sharedPreferences.edit()                   //sharedPreferences를 조작할 때 사용 -> 저장 등 할 때 사용
        val songToJson = gson.toJson(song)                     //song 데이터 객체를 Json 형태로 저장해줄것임
        editor.putString("song", songToJson)                    //Gson 이 객체를 String 형식으로 한줄로 정렬
        editor.apply()

    }

    override fun onDestroy() {
        super.onDestroy()
        player.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

}

