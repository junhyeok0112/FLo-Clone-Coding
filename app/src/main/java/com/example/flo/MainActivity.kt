package com.example.flo

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.Data.Album
import com.example.flo.Data.Song
import com.example.flo.homefragment.HomeFragment
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.lockerfragment.LockerFragment
import com.example.flo.look.LookFragment
import com.google.gson.Gson
import java.text.SimpleDateFormat

//import com.example.flo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), onAlbumClickListener  {

    lateinit var binding: ActivityMainBinding
    lateinit var activityLaucher : ActivityResultLauncher<Intent>
    lateinit var player:Player
    var isPlaying = false       //Main에서 MiniPlayer가 실행되고있는지 아닌지 체크
    //Gson 선언
    private var gson:Gson = Gson()
    private var nowsong: Song = Song()
    private var mediaPlayer : MediaPlayer? = null
    private lateinit var songDB: SongDatabase

    private var albumSongs : ArrayList<Song> = ArrayList()       //앨범눌렀을 때 저장될 노래들
    var pos = 0                                     //앨범에서 몇번째 노래가 재생되고 있는지.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songDB = SongDatabase.getInstance(this)!!
        initNavigation()
        initListener()
        inputDummyAlbums()
        inputDummySongs()
    }


    override fun onAlbumClick(position: Int) {   //클릭시 해당 position이 넘어옴 단 0부터 시작
        //앨범 새로 클릭했으면 나오는 노래 다 멈춤
        val songDB = SongDatabase.getInstance(this)!!       //어짜피 DB는 하나
        val album = songDB.albumDao().getAlbum(position+1)      //position + 1에 해당하는 앨범 나옴
        val songs = songDB.songDao().getSongs()
        albumSongs.clear()                                          //이전 앨범에 있는 노래 비움
        for(nowSong : Song in songs){                               //앨범 ID와 동일한 albumIndex를 가지는 노래들만 List에 추가
            if(album.id == nowSong.albumIdx){
                albumSongs.add(nowSong)
            }
        }
        Log.d("Album List" , "${albumSongs.toString()}")
        //Load 시키고 Start 해야함,
        mediaPlayer?.release()
        mediaPlayer = null
        player.isPlaying = false
        isPlaying = false
        nowsong.isPlaying = false
        setChangePlay(false)
        pos = 0                             //pos 값 초기화
        nowsong = albumSongs.get(pos)
        setMiniPlayer(nowsong)

    }
    //MiniPlayer에 노래 셋팅
    fun setMiniPlayer(song : Song){     //넘어온 Song의 데이터 저장
        Log.d("Main","setMiniPlayer 셋팅 : ${song}")
        nowsong = song
        binding.mainTitleTv.text = song.title
        binding.mainSinggerTv.text = song.singger

        val music = resources.getIdentifier(song.music,  "raw",this.packageName )
        mediaPlayer = MediaPlayer.create(this,music)
        binding.mainSeekBarSb.max = mediaPlayer?.duration!!
        binding.mainSeekBarSb.progress = (song.second * 1000)
        mediaPlayer?.seekTo(song.second * 1000)

        if(song.isPlaying){
            binding.mainMiniplayerPause.visibility = View.VISIBLE
            binding.mainMiniplayerBtn.visibility = View.GONE
        } else{
            binding.mainMiniplayerPause.visibility = View.GONE
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
        }

       startPlayer()
    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment(this))
            .commitAllowingStateLoss()
    }

    private fun inputDummyAlbums(){
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        if(albums.isNotEmpty()) return                              //이미 앨범 정보가 있으면 리턴

        songDB.albumDao().insert(
            Album(
                1,
                "IU 5th Album 'LILAC'", "아이유 (IU)", R.drawable.img_album_exp2
            )
        )

        songDB.albumDao().insert(
            Album(
                2,
                "Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp
            )
        )

        songDB.albumDao().insert(
            Album(
                3,
                "iScreaM Vol.10 : Next Level Remixes", "에스파 (AESPA)", R.drawable.img_album_exp3
            )
        )

        songDB.albumDao().insert(
            Album(
                4,
                "MAP OF THE SOUL : PERSONA", "방탄소년단 (BTS)", R.drawable.img_album_exp4
            )
        )

        songDB.albumDao().insert(
            Album(
                5,
                "GREAT!", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5
            )
        )

    }

    //ROOM_DB
    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if (songs.isNotEmpty()) return

        songDB.songDao().insert(
            Song(
                "Lilac",
                "아이유 (IU)",
                0,
                215,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                "",
                false,
                1
            )
        )

        songDB.songDao().insert(
            Song(
                "Flu",
                "아이유 (IU)",
                0,
                189,
                false,
                "music_flu",
                R.drawable.img_album_exp2,
                "",
                false,
                1
            )
        )

        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단 (BTS)",
                0,
                196,
                false,
                "music_celebrity",
                R.drawable.img_album_exp,
                "",
                false,
                2
            )
        )

        songDB.songDao().insert(
            Song(
                "Butter (Hotter Remix)",
                "방탄소년단 (BTS)",
                0,
                139,
                false,
                "music_empty_cup",
                R.drawable.img_album_exp,
                "",
                false,
                2
            )
        )

        songDB.songDao().insert(
            Song(
                "Butter (Sweeter Remix)",
                "방탄소년단 (BTS)",
                0,
                229,
                false,
                "music_epilogue",
                R.drawable.img_album_exp,
                "",
                false,
                2
            )
        )

        songDB.songDao().insert(
            Song(
                "Next Level",
                "에스파 (AESPA)",
                0,
                222,
                false,
                "music_next_level",
                R.drawable.img_album_exp3,
                "",
                false,
                3
            )
        )

        songDB.songDao().insert(
            Song(
                "Next Level (IMLAY Remix)",
                "에스파 (AESPA)",
                0,
                325,
                false,
                "music_hi_spring_bye",
                R.drawable.img_album_exp3,
                "",
                false,
                3
            )
        )

        songDB.songDao().insert(
            Song(
                "Boy with Luv",
                "방탄소년단 (BTS)",
                0,
                316,
                false,
                "music_my_sea",
                R.drawable.img_album_exp4,
                "",
                false,
                4
            )
        )

        songDB.songDao().insert(
            Song(
                "소우주 (Mikrokosmos)",
                "방탄소년단 (BTS)",
                0,
                189,
                false,
                "music_troll",
                R.drawable.img_album_exp4,
                "",
                false,
                4
            )
        )

        songDB.songDao().insert(
            Song(
                "Make It Right",
                "방탄소년단 (BTS)",
                0,
                215,
                false,
                "music_lilac",
                R.drawable.img_album_exp4,
                "",
                false,
                4
            )
        )

        songDB.songDao().insert(
            Song(
                "BBoom BBoom",
                "모모랜드 (MOMOLAND)",
                0,
                193,
                false,
                "music_coin",
                R.drawable.img_album_exp5,
                "",
                false,
                5
            )
        )

        songDB.songDao().insert(
            Song(
                "궁금해",
                "모모랜드 (MOMOLAND)",
                0,
                188,
                false,
                "music_flu",
                R.drawable.img_album_exp5,
                "",
                false,
                5
            )
        )


        val _songs = songDB.songDao().getSongs()
        Log.d("DB DATA", _songs.toString())

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
        var second = nowsong.second
        override fun run() {    //이 안에 코드가 start되면 시작
            try{
                if(mediaPlayer == null) return
                while (true){
                    if(second >= playTime && pos >= albumSongs.size){     //시간 다 됐는데 , 마지막 곡이면 멈춰
                        Log.d("Main Thread" , "Thread 종료 , ${pos}" )
                        break
                    }
                    if(second>=playTime && isPlaying){                         //끝까지 갔는데 , 앨범에 리스트 남아있는경우
                        Log.d("Main","노래 초기화 시작")
                        pos = if (pos == albumSongs.size - 1) 0
                        else pos + 1
                        player.interrupt()
                        mediaPlayer!!.release()         //노래 끝나면 해제했다가 다시
                        mediaPlayer = null
                        runOnUiThread {
                            Log.d("Main", "Thread 내부에서 setMiniPlayer 실행")
                            songDB.songDao().updateSecondById(0, nowsong.id)
                            nowsong = albumSongs.get(pos)                                   //다음 곡을nowsong으로
                            setMiniPlayer(nowsong)
                        }
                    }
                    if(isPlaying){
                        Log.d("Main","${second}")
                        sleep(1000)
                        second++
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

    fun startPlayer(){
        player = Player(nowsong.playTime , nowsong.isPlaying )
        player.start()
    }

    private fun moveSong(direct : Int){
        if (pos + direct < 0){
            Toast.makeText(this,"first song", Toast.LENGTH_SHORT).show()
            return
        }
        if (pos + direct >= albumSongs.size){
            Toast.makeText(this,"last song", Toast.LENGTH_SHORT).show()
            return
        }
        //이런 DB작업들 나중에 전부 Thread 처리
        songDB.songDao().updateSecondById(0,nowsong.id)           //다음곡으로 넘어가면 이전 곡 Second 0 으로
        pos += direct
        player.interrupt()
        startPlayer()
        mediaPlayer?.release() // 미디어플레이어가 가지고 있던 리소스를 해방
        mediaPlayer = null // 미디어플레이어 해제
        nowsong = albumSongs[pos]
        setMiniPlayer(nowsong)

    }

    private fun initListener(){
        //SeekBar 변화시 적용
        binding.mainSeekBarSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser){
                    mediaPlayer?.seekTo(progress)
                    //Toast.makeText(this@MainActivity , "${progress}" , Toast.LENGTH_SHORT).show()
                    player.second = progress / 1000             //Player에 변경된 초도 갱신해주어야지 다음곡으로 넘어감
                    nowsong.second = progress / 1000
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //터치했을 때
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //터치 끝났을 때
            }
        })

//        //콜백 메서드 설정 -> onActivityResult와 같은역할
//        activityLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
//            if(result.resultCode == RESULT_OK) {//노래는 onStart 에서 셋팅 됨
//                Log.d("Song 에서 Main으로" , "${nowsong.isPlaying}")
//                player.isPlaying = nowsong.isPlaying
//                isPlaying = nowsong.isPlaying
//                setChangePlay(nowsong.isPlaying)
//                if(nowsong.isPlaying) {                     //계속 플레이중일떄
//                    mediaPlayer?.start()
//                }
//            }
//        }


        //미니 플레이어 클릭시 실행
        binding.mainPlayerLayout.setOnClickListener {
            val songDB = SongDatabase.getInstance(this)!!
            songDB.songDao().updateSecondById(binding.mainSeekBarSb.progress / 1000 , nowsong.id)
            songDB.songDao().updateIsPlayingById(nowsong.isPlaying, nowsong.id)             //플레이상태 전달
            val intent = Intent(this,SongActivity::class.java)
            val nowSongToJson = gson.toJson(nowsong)
            intent.putExtra("nowsong",nowSongToJson)
            intent.putExtra("songId" , nowsong.id)
            player.interrupt()
            mediaPlayer?.release()
            mediaPlayer = null
            //activityLaucher.launch(intent)      //startActivityForResult 대신 실행
            startActivity(intent)
        }

        //미니 플레이어 버튼 눌렀을 시 변경
        binding.mainMiniplayerBtn.setOnClickListener {
            player.isPlaying = true
            isPlaying = true
            nowsong.isPlaying = true
            mediaPlayer?.start()
            setChangePlay(true)
        }

        binding.mainMiniplayerPause.setOnClickListener {
            player.isPlaying = false
            isPlaying = false
            nowsong.isPlaying = false
            mediaPlayer?.pause()
            setChangePlay(false)
        }

        binding.mainNextIv.setOnClickListener {
            moveSong(1)
        }

        binding.mainPreIv.setOnClickListener {
            moveSong(-1)
        }

        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment(this))
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


    override fun onStart() {        //SharedPreference에 저장된 값들 가져오는 역할 , UI 관련 함수 등 메소드 초기화
        super.onStart()
        Log.d("Main onStart" , "onStart실행")
        val spf = getSharedPreferences("song" , MODE_PRIVATE)
        val songId = spf.getInt("songId",0)                       //SongActivity나 실행될때 하나의 노래 셋팅
        val songDB = SongDatabase.getInstance(this)!!
        nowsong =  if(songId == 0){                                                    //우선 노래만 가져오게함 -> 이후 이 노래가 해당하는 앨범 ->
            songDB.songDao().getSong(1)
        }else{
            songDB.songDao().getSong(songId)
        }
        Log.d("Main의 onstart", "${nowsong}")
        setMiniPlayer(nowsong)

        //노래가 들어있는 앨범리스트 다시 적용해줘야함
        albumSongs.clear()                                          //이전 앨범에 있는 노래 비움
        albumSongs = songDB.songDao().getSongsInAlbum(nowsong.albumIdx) as ArrayList
        pos = getPlaySongPosition(nowsong.id)                       //여기까지하면 지금 노래가 앨범에서 몇번째인지 , 해당앨범에 있는 노래 전부가져옴

        player.isPlaying = nowsong.isPlaying
        isPlaying = nowsong.isPlaying
        setChangePlay(nowsong.isPlaying)
        if(nowsong.isPlaying) {                     //계속 플레이중일떄
            mediaPlayer?.start()
        }

    }

    fun getPlaySongPosition(songId: Int) : Int{               //주어진 노래가 앨범에서 몇번째인지 셋팅
        for (i in 0 until albumSongs.size){                        //리스트에서 해당하는 노래의 nowPos(몇번째인지 추출)
            if (albumSongs[i].id == songId){
                return i
            }
        }
        return 0
    }

    override fun onPause() {         //액티비티가 포커스 잃으면 노래 중지
        super.onPause()
        mediaPlayer?.pause()        //미디어 플레이어 중지
        player.isPlaying = false    //쓰레드 중지
        nowsong.isPlaying = false      //중지되었으니까 멈춤
        setChangePlay(false)
        //앱이 종료되었다가 다시 시작하면 종료된 시점부터 사용하기 위해 종료 시점을 sharedPreferences 로 저

        val sharedPreferences = getSharedPreferences("song" , MODE_PRIVATE)
        val editor = sharedPreferences.edit()                   //sharedPreferences를 조작할 때 사용 -> 저장 등 할 때 사용

        editor.putInt("songId",nowsong.id)                //ID로 정보 전달 , Activity 전환되면서 현재 노래의 id 전달
        editor.apply()

    }

    override fun onDestroy() {
        super.onDestroy()
        player.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

}

