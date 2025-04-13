package com.example.myapplication_ass2.repository

import com.example.myapplication_ass2.network.DrugApiService

// FakeDrugRepository 直接继承生产代码的 DrugRepository
class FakeDrugRepository(apiService: DrugApiService) : DrugRepository(apiService)
