package com.example.myapplication_ass2.network

import retrofit2.http.GET
import retrofit2.http.Query

data class DrugInfo(
    val name: String,
    val description: String,
    val side_effects: List<String>,
    val usage: String
)

interface DrugApiService {
    @GET("data.json")
    suspend fun getDrugInfo(@Query("name") name: String): List<DrugInfo>
}
