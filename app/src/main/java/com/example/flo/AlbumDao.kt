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

//    fun songAdd(album:Album , song:Song){
//        album.songs.add(song)
//    }

}