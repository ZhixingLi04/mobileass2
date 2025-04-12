package com.example.myapplication_ass2.di

import com.example.myapplication_ass2.network.DrugApiService
import com.example.myapplication_ass2.repository.DrugRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DrugContainer {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://zhixingli04.github.io/Medical-API/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService: DrugApiService = retrofit.create(DrugApiService::class.java)

    val repository = DrugRepository(apiService)
}
