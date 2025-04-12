package com.example.myapplication_ass2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medication_table")
data class MedicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dosage: String,
    val time: String,
    val taken: Boolean = false
)
