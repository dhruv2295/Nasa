package com.nasa.image.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nasa.image.repo.NasaRepository

class MainViewModelFactory (private val nasaRepository: NasaRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(nasaRepository) as T
    }
}