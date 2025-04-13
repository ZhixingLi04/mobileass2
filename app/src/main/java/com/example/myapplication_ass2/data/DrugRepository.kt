package com.example.myapplication_ass2.repository

import com.example.myapplication_ass2.network.DrugApiService
import com.example.myapplication_ass2.network.DrugInfo

open class DrugRepository(private val apiService: DrugApiService) {
    open suspend fun fetchDrugInfo(name: String): Result<List<DrugInfo>> {
        return try {
            val info = apiService.getDrugInfo(name)
            Result.success(info)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
