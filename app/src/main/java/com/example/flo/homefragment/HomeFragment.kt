package com.example.flo.homefragment

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.Slide
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.R
import com.example.flo.Song
import com.example.flo.adapter.BannerViewpagerAdapter
import com.example.flo.adapter.PanelViewpagerAdapter
import com.example.flo.albumfragment.AlbumFragment
import com.example.flo.databinding.FragmentHomeBinding
//import com.example.flo.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var slideBanner: SlideBanner
    lateinit var slidePannel: SlidePannel
    lateinit var handler : Handler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setViewpager()
        handler = Handler(Looper.getMainLooper())

        slideBanner = SlideBanner()
        slidePannel = SlidePannel()
        slideBanner.start()
        slidePannel.start()


        //오늘 앨범 누르면 이동
        binding.homeTodayAlbum1Container.setOnClickListener {
            moveAlbumFragment(1)
        }

        binding.homeTodayAlbum2Container.setOnClickListener {
            moveAlbumFragment(2)
        }

        binding.homeTodayAlbum3Container.setOnClickListener {
            moveAlbumFragment(3)
        }

        binding.homeTodayAlbum4Container.setOnClickListener {
            moveAlbumFragment(4)
        }



        return binding.root
    }


    fun moveAlbumFragment(num:Int){        //앨범 눌렀을때 제목과 가수를 album_fragment로 보내기
        val song = Song()
        var imgRes : Bitmap? = null
        when(num){
            1->{
                song.title = binding.homeTodayAlbum1Title.text.toString()
                song.singger = binding.homeTodayAlbum1Singger.text.toString()
                imgRes = binding.homeTodayAlbum1Ssumnail.drawable.toBitmap()
            }
            2->{
                song.title = binding.homeTodayAlbum2Title.text.toString()
                song.singger = binding.homeTodayAlbum2Singger.text.toString()
                imgRes = binding.homeTodayAlbum2Ssumnail.drawable.toBitmap()
            }
            3->{
                song.title = binding.homeTodayAlbum3Title.text.toString()
                song.singger = binding.homeTodayAlbum3Singger.text.toString()
                imgRes = binding.homeTodayAlbum3Ssumnail.drawable.toBitmap()
            }
            4->{
                song.title = binding.homeTodayAlbum4Title.text.toString()
                song.singger = binding.homeTodayAlbum4Singger.text.toString()
                imgRes = binding.homeTodayAlbum4Ssumnail.drawable.toBitmap()
            }
        }
        //song.imgId = binding.homeTodayAlbum1Ssumnail.sourceLayoutResId.toString()

        setFragmentResult("requestKey", bundleOf(
            "title" to song.title,
            "singger" to song.singger,
            "imgRes" to imgRes
        ))

        parentFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment())
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