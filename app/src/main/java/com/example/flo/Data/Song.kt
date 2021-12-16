package com.example.flo.Data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "SongTable")
data class Song(
    var title: String = "",
    @SerializedName("singer") var singger: String = "",
    var second: Int = 0,
    var playTime: Int = 0,
    var isPlaying: Boolean = false,
    var music: String = "",
    var coverImg: Int? = null,
    var coverImgUrl: String? = null,
    var isLike: Boolean = false,
    var albumIdx: Int = 0,  // 이 song이 어떤 앨범에 담겨 있는지 가리키는 변수 (foreign key 역할)
    var isTitleSong:String =""
){
    @SerializedName("songIdx") @PrimaryKey(autoGenerate = true) var id: Int = 0
}