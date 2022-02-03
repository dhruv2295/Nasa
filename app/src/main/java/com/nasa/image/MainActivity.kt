package com.nasa.image

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nasa.image.ActivityFunctionManager.MainManager
import com.nasa.image.adapter.NasaListAdapter
import com.nasa.image.database.Nasa
import com.nasa.image.database.NasaDatabase
import com.nasa.image.databinding.ActivityMainBinding
import com.nasa.image.network.NetworkManager
import com.nasa.image.repo.NasaRepository
import com.nasa.image.viewModels.MainViewModel
import com.nasa.image.viewModels.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var nasaListAdapter: NasaListAdapter
    private lateinit var networkManager: NetworkManager
    private lateinit var mainManager: MainManager
    private var nasaList = mutableListOf<Nasa>()
    private var isAppFirstStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = NasaDatabase.getDatabase(applicationContext).nasaDao()
        val repository = NasaRepository(dao)

        mainManager = MainManager(this)
        nasaListAdapter = NasaListAdapter(nasaList)
        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]

        networkManager = NetworkManager(this, object: NetworkManager.IAPICallback{
            override fun onSuccess(isSuccess: Boolean, list: List<Nasa>) {
                binding.progressBar.visibility = View.GONE
                binding.request.visibility = View.GONE
                binding.refresh.isRefreshing = false
                binding.request.isClickable = true

                if (isSuccess) {
                    if (isAppFirstStart){
                        mainViewModel.clearDB()
                        list.map { mainViewModel.insertNasa(it)}
                    } else{
                        nasaList.clear()
                        nasaList.addAll(list)
                        nasaListAdapter.notifyDataSetChanged()
                    }
                }
                isAppFirstStart = false
            }

            override fun onFailure(errorMessage: String) {
                binding.request.isClickable = true
                binding.progressBar.visibility = View.GONE
                binding.request.visibility = View.GONE
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })

        mainViewModel.getNasa().observe(this) {
            binding.progressBar.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            binding.request.visibility = View.GONE
            nasaList.clear()
            nasaList.addAll(it)
            nasaListAdapter.notifyDataSetChanged()
        }

        binding.nasaRecyclerView.apply {
            adapter = nasaListAdapter
            layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        }

        binding.sDate.text = mainManager.getLastMonth()
        binding.eDate.text = mainManager.getToday()

        binding.editEnd.setOnClickListener {
            mainManager.showDateTimePicker(
                binding.eDate,
                binding.request
            )
        }

        binding.editStart.setOnClickListener {
            mainManager.showDateTimePicker(
                binding.sDate,
                binding.request
            )
        }

        binding.request.setOnClickListener {
            binding.request.isClickable = false
            binding.progressBar.visibility = View.VISIBLE
            networkManager.getImageOfTheDay(
                startDate = binding.eDate.text.toString(),
                endDate = binding.sDate.text.toString()
            )
        }

        binding.search.doOnTextChanged { text, _, _, _ ->

            val l = mutableListOf<Nasa>()
            nasaList.map {
                if (it.title!!.lowercase().contains(text.toString().lowercase())){
                    l.add(it)
                }
            }
            nasaListAdapter.updateList(l)
        }

        binding.refresh.setOnRefreshListener {
            networkManager.getImageOfTheDay(
                startDate = mainManager.getLastMonth(),
                endDate = mainManager.getToday()
            )
            isAppFirstStart = true
        }

        networkManager.getImageOfTheDay(
            startDate = mainManager.getLastMonth(),
            endDate = mainManager.getToday()
        )
    }
}