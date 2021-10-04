package com.example.flo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.flo.databinding.FragmentSongListBinding

class SongListFragment : Fragment() {

    //앨범 프래그먼트에서 이으려면 SongList 프래그먼트랑 이어야함
    var _binding : FragmentSongListBinding? = null
    val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSongListBinding.inflate(inflater , container , false)

        //클릭시 제목 Toast로 띄우기
        binding?.songlistList1ContainerCl?.setOnClickListener{
            Toast.makeText(context, "${binding?.songlistList1TitleTv?.text}" , Toast.LENGTH_SHORT).show()
        }

        binding?.songlistList2ContainerCl?.setOnClickListener{
            Toast.makeText(context, "${binding?.songlistList2TitleTv?.text}" , Toast.LENGTH_SHORT).show()
        }

        binding?.songlistList3ContainerCl?.setOnClickListener{
            Toast.makeText(context, "${binding?.songlistList3TitleTv?.text}" , Toast.LENGTH_SHORT).show()
        }

        binding?.songlistList4ContainerCl?.setOnClickListener{
            Toast.makeText(context, "${binding?.songlistList4TitleTv?.text}" , Toast.LENGTH_SHORT).show()
        }

        //토글 클릭시 변경
        binding?.songlistToggleIv?.setOnClickListener {
            setChangToggle(false)
        }

        binding?.songlistToggleOnIv?.setOnClickListener {
            setChangToggle(true)
        }



        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setChangToggle(isOn : Boolean){
        when(isOn){
            true->{
                binding?.songlistToggleIv?.visibility = View.VISIBLE
                binding?.songlistToggleOnIv?.visibility = View.GONE
            }
            false->{
                binding?.songlistToggleIv?.visibility = View.GONE
                binding?.songlistToggleOnIv?.visibility = View.VISIBLE
            }
        }
    }


}