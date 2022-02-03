package com.nasa.mvvm.network

import android.app.Activity
import com.nasa.mvvm.dataModel.NasaResponse
import com.nasa.mvvm.database.Nasa
import com.nasa.image.utils.AppConstraint.Companion.API_KEY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkManager(val applicationContext: Activity, val iAPICallback: IAPICallback) {

    fun getImageOfTheDay(startDate: String = "2022-01-01", endDate: String = "2022-02-01"){
        with(
            NetworkService,
        ) {
            apiInterface.getApod(API_KEY, startDate, endDate)
                .enqueue(
                    object : Callback<List<NasaResponse>> {
                        override fun onResponse(
                            call: Call<List<NasaResponse>>,
                            response: Response<List<NasaResponse>>
                        ) {
                            val list = mutableListOf<Nasa>()
                            for (i in response.body()!!.indices) {
                                list.add(Nasa(i, response.body()!![i].date,
                                    response.body()!![i].explanation,
                                    response.body()!![i].hdurl,
                                    response.body()!![i].media_type,
                                    response.body()!![i].service_version,
                                    response.body()!![i].title,
                                    response.body()!![i].url
                                ))
                            }
                            iAPICallback.onSuccess(response.isSuccessful, list)
                        }

                        override fun onFailure(call: Call<List<NasaResponse>>, t: Throwable) {
                            t.message?.let { iAPICallback.onFailure(it) }
                        }
                    },
                )
        }
    }

    interface IAPICallback{
        fun onSuccess(isSuccess: Boolean, list: List<Nasa>)
        fun onFailure(errorMessage: String)
    }
}