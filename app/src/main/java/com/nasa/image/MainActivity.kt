package com.nasa.image

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.nasa.image.ActivityFunctionManager.MainManager
import com.nasa.image.adapter.NasaListAdapter
import com.nasa.image.database.Nasa
import com.nasa.image.databinding.ActivityMainBinding
import com.nasa.image.network.NetworkManager
import com.nasa.image.viewModels.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var nasaListAdapter: NasaListAdapter
    private lateinit var networkManager: NetworkManager
    private lateinit var mainManager: MainManager
    private var nasaList = mutableListOf<Nasa>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainManager = MainManager(this)
        nasaListAdapter = NasaListAdapter(nasaList)

        networkManager = NetworkManager(this, object: NetworkManager.IAPICallback{
            override fun onSuccess(isSuccess: Boolean, list: List<Nasa>) {
                binding.progressBar.visibility = View.GONE
                binding.request.visibility = View.GONE
                binding.refresh.isRefreshing = false
                binding.request.isClickable = true
                if (isSuccess) {
                    nasaList.clear()
                    nasaList.addAll(list)
                    nasaListAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(errorMessage: String) {
                binding.request.isClickable = true
                binding.progressBar.visibility = View.GONE
                binding.request.visibility = View.GONE
                binding.refresh.isRefreshing = false
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })

        binding.nasaRecyclerView.apply {
            adapter = nasaListAdapter
            layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
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

        networkManager.getImageOfTheDay(
            startDate = mainManager.getLastMonth(),
            endDate = mainManager.getToday()
        )
    }
}