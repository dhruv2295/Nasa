package com.nasa.image.repo

import androidx.lifecycle.LiveData
import com.nasa.image.database.Nasa
import com.nasa.image.database.NasaDao

class NasaRepository(private val nasaDao: NasaDao) {

    fun getNasaList(): LiveData<List<Nasa>> {
        return nasaDao.getNasa()
    }

    suspend fun insertNasa(quote: Nasa) {
        nasaDao.insertNasa(quote)
    }

    fun clearDB(){
        nasaDao.clearDB()
    }
}