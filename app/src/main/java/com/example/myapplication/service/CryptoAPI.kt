package com.example.myapplication.service

import com.example.myapplication.model.CryptoModel
import retrofit2.Call
import retrofit2.http.GET

interface CryptoAPI {

    @GET("atilsamancioglu/K21-JSONDataSet/master/crypto.json")
    fun getData () : Call<List<CryptoModel>>
}