package com.nasa.image.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NasaDao {

    @Query("SELECT * from nasa")
    fun getNasa() : LiveData<List<Nasa>>

    @Insert
    suspend fun insertNasa(quote: Nasa)

    @Query("DELETE from nasa")
    fun clearDB()
}