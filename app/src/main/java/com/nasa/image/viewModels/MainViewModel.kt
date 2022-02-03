package com.nasa.image.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasa.image.database.Nasa
import com.nasa.image.repo.NasaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val nasaRepository: NasaRepository) : ViewModel() {

    fun getNasa() : LiveData<List<Nasa>> {
        return nasaRepository.getNasaList()
    }

    fun insertNasa(nasa: Nasa){
        viewModelScope.launch(Dispatchers.IO){
            nasaRepository.insertNasa(nasa)
        }
    }

    fun clearDB(){
        viewModelScope.launch(Dispatchers.IO){
            nasaRepository.clearDB()
        }
    }
}