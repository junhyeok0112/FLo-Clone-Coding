package com.example.flo

data class Song (
    var title:String ="",
    var singger:String="",
    var imgId : String="",
    var playTime:Int = 0,
    var isPlaying: Boolean = false,
    var music:String ="",       //어떤 음악이 재생되고 있는지
    var currentPosition : Int = 0


)