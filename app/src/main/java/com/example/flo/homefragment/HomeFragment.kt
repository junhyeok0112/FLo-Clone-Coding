package com.example.flo.homefragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.Data.Album
import com.example.flo.Data.Song
import com.example.flo.MainActivity
import com.example.flo.R
import com.example.flo.SongDatabase
import com.example.flo.adapter.AlbumRVAdapter
import com.example.flo.adapter.BannerViewpagerAdapter
import com.example.flo.adapter.PanelViewpagerAdapter
import com.example.flo.albumfragment.AlbumFragment
import com.example.flo.databinding.FragmentHomeBinding
import com.example.flo.onAlbumClickListener
import com.google.gson.Gson

//import com.example.flo.databinding.FragmentHomeBinding


class HomeFragment(var mContext:MainActivity) : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var slideBanner: SlideBanner
    lateinit var slidePannel: SlidePannel
    lateinit var handler : Handler

    private lateinit var mAlbumClickListener: onAlbumClickListener

    private var albums = ArrayList<Album>()
    private lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setViewpager()
        handler = Handler(Looper.getMainLooper())

        //ROOM_DB
        songDB = SongDatabase.getInstance(requireContext())!!
        albums.addAll(songDB.albumDao().getAlbums()) // songDB에서 album list를 가져옵니다.

        //인터페이스 함수를 구현한 객체(MainActivity)로 클릭리스너 인터페이스 객체 초기화
        //이렇게 해야 처리하고자 하는 화면의 정의해놓은 인터페이스 함수를 호출 할 수 있음
        mAlbumClickListener = mContext


        val albumAdpater = AlbumRVAdapter(albums)
        binding.homeTodayRv.adapter = albumAdpater

        //외부에서 리스너 객체 전달
        albumAdpater.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener{    //Adapter가 클릭됐을때 하는 행동정의

            override fun onItemClick(album: Album) { //onClick시에 Album을 넣으면 자동으로 선택된 Album이 매개변수로 ?
                //-> Adapter에 있는 position값과 같이 HomeFragment로 넘어와서 자동 셋팅
                //프래그먼트끼리 데이터 저장시 Bundle 사용 , Bundle을 Arguments로 넘겨줘야함
                //changeAlbumFragemnt(album)

            }

            override fun onStartSong(position: Int) {
                mAlbumClickListener.onAlbumClick(position)
            }
            //구현해야 할거 : 클릭했을 때 그 앨범의 노래들을 List에 넣어야함 -> 노래 List들 만들어야함 ,
            //데모버전으로 노래 3곡정도만 정해서 모든 앨범 눌렀을 시 실행되게 해야함
            //클릭시 Album정보를 MainActivity로 넘겨서 셋팅해야함
        })
        //레이아웃 매니저 설정 , XML에서도 가능
        binding.homeTodayRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        slideBanner = SlideBanner()
        slidePannel = SlidePannel()
        slideBanner.start()
        slidePannel.start()

        return binding.root
    }

    private fun changeAlbumFragemnt(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumToJson = gson.toJson(album)
                    putString("album", albumToJson)
                }
            })
            .commitAllowingStateLoss()
    }


    fun setViewpager(){
        //BannerViewpagerAdapter 추가
        val bannerViewpagerAdapter = BannerViewpagerAdapter(this)
        bannerViewpagerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerViewpagerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerViewpagerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerViewpagerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerViewpagerVp.adapter = bannerViewpagerAdapter
        binding.homeBannerViewpagerVp.offscreenPageLimit = 4


        val panelViewpagerAdapter = PanelViewpagerAdapter(this)
        panelViewpagerAdapter.addFragment(PanelFragment(R.drawable.img_default_4_x_1))
        panelViewpagerAdapter.addFragment(PanelFragment(R.drawable.img_default_4_x_2))
        panelViewpagerAdapter.addFragment(PanelFragment(R.drawable.img_default_4_x_3))
        panelViewpagerAdapter.addFragment(PanelFragment(R.drawable.img_default_4_x_4))
        panelViewpagerAdapter.addFragment(PanelFragment(R.drawable.img_default_4_x_5))
        panelViewpagerAdapter.addFragment(PanelFragment(R.drawable.img_default_4_x_1))
        panelViewpagerAdapter.addFragment(PanelFragment(R.drawable.img_default_4_x_2))
        panelViewpagerAdapter.addFragment(PanelFragment(R.drawable.img_default_4_x_3))
        panelViewpagerAdapter.addFragment(PanelFragment(R.drawable.img_default_4_x_4))
        panelViewpagerAdapter.addFragment(PanelFragment(R.drawable.img_default_4_x_5))
        binding.homePanelViewpagerVp.adapter = panelViewpagerAdapter
        binding.homePanelViewpagerVp.offscreenPageLimit = 10
        binding.homePanelViewpagerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        //PanelViewpager에서 indicator 추가
        binding.homePanelIndicator.setViewPager(binding.homePanelViewpagerVp)
        binding.homePanelIndicator.createIndicators(10,0)

        //PanelViewpager가 변경될 때마다 우리가 알 수 있도록 Calback호출 ->여기서 indicator 위치 바꿔줌
        binding.homePanelViewpagerVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.homePanelIndicator.animatePageSelected(position)
                //현재 바뀐 페이지를 쓰레드에도 알려줘야함
                slidePannel.setPosition(position)
            }
        })

        binding.homeBannerViewpagerVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                slideBanner.setPosition(position)
            }
        })
    }

    inner class SlideBanner : Thread(){
        private var currentPosition = 0
        override fun run() {
            try{
                while(true){
                    if(currentPosition >= 4) currentPosition = 0    //한바퀴 돌았으면 초기화
                    sleep(3000)
                    handler.post {
                        binding.homeBannerViewpagerVp.setCurrentItem(currentPosition, true)
                    }
                    currentPosition++
                }
            }catch(e: InterruptedException){
                Log.d("Thread Interrupt" , "SlideBanner 쓰레드 종료")
            }
        }
        fun setPosition(position:Int){
            currentPosition = position
        }
    }

    inner class SlidePannel : Thread(){
        private var currentPosition = 0
        override fun run() {
            try{
                while(true){
                    if(currentPosition >= 10) currentPosition = 0    //한바퀴 돌았으면 초기화
                    sleep(3000)
                    handler.post{
                        binding.homePanelViewpagerVp.setCurrentItem(currentPosition, true)
                    }
                    currentPosition++
                }
            }catch (e:InterruptedException){
                Log.d("Thread Interrupt" , "SlidePannel 쓰레드 종료")
            }

        }

        fun setPosition(position:Int){
            currentPosition = position
        }
    }

    override fun onDestroy() {
        slidePannel.interrupt()
        slideBanner.interrupt()
        super.onDestroy()
    }
}