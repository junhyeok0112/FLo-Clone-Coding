package com.example.flo

import androidx.room.*
import com.example.flo.Data.Album
import com.example.flo.Data.Song


@Dao
interface AlbumDao {
    @Insert
    fun insert(album: Album)

    @Update
    fun update(album: Album)

    @Delete
    fun delete(album: Album)

    @Query("SELECT * FROM AlbumTable") // 테이블의 모든 값을 가져와라
    fun getAlbums(): List<Album>

    @Query("SELECT * FROM AlbumTable WHERE id = :id")
    fun getAlbum(id: Int): Album

    @Insert
    fun likeAlbum(like:Like)

    @Query("SELECT id FROM LikeTable WHERE userId = :userId AND albumId = :albumId")    //라이크 테이블의 아이디값 리턴
    fun isLikeAlbum(userId: Int , albumId: Int) : Int?

    @Query("DELETE FROM LikeTable WHERE userId = :userId AND albumId = :albumId")    //라이크 테이블의 아이디값 리턴
    fun disLikeAlbum(userId: Int , albumId: Int) : Int?

    @Query("SELECT AT.* FROM LikeTable as LT LEFT JOIN AlbumTable as AT ON LT.albumId = AT.id WHERE LT.userId =:userId")
    fun getLikeAlbum(userId : Int) :List<Album>         //유저가 좋아하는 앨범들 전부 리턴

//    fun songAdd(album:Album , song:Song){
//        album.songs.add(song)
//    }

}