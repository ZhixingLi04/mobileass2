package com.example.myapplication_ass2.repository

import com.example.myapplication_ass2.network.DrugApiService

class FakeDrugRepository(apiService: DrugApiService) : DrugRepository(apiService)
