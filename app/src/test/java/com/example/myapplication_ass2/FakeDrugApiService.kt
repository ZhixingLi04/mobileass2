package com.example.myapplication_ass2.network

class FakeDrugApiService : DrugApiService {
    override suspend fun getDrugInfo(name: String): List<DrugInfo> {
        return listOf(
            DrugInfo(
                name = name,
                description = "Description for $name",
                side_effects = listOf("Nausea", "Headache"),
                usage = "Usage for $name"
            )
        )
    }
}
