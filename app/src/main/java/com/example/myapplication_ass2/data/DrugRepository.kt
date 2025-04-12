package com.example.myapplication_ass2.repository

import com.example.myapplication_ass2.network.DrugApiService
import com.example.myapplication_ass2.network.DrugInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DrugRepository(private val api: DrugApiService) {

    // 修改返回类型为 Result<List<DrugInfo>>
    suspend fun fetchDrugInfo(name: String): Result<List<DrugInfo>> {
        return try {
            val response = withContext(Dispatchers.IO) {
                api.getDrugInfo(name)
            }
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
