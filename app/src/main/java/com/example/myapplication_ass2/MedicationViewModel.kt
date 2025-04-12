package com.example.myapplication_ass2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MedicationViewModel(application: Application) : AndroidViewModel(application) {
    // 创建数据库实例，添加 fallbackToDestructiveMigration() 以消除版本迁移异常
    private val database = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "eldercare_database"
    )
        .fallbackToDestructiveMigration()
        .build()

    private val medicationDao = database.medicationDao()

    private val _medications = MutableStateFlow<List<MedicationEntity>>(emptyList())
    val medications: StateFlow<List<MedicationEntity>> = _medications

    init {
        viewModelScope.launch {
            medicationDao.getAllMedications().collect { meds ->
                _medications.value = meds
            }
        }
    }

    fun markAsDone(medication: MedicationEntity) {
        viewModelScope.launch {
            val updatedMed = medication.copy(taken = true)
            medicationDao.updateMedication(updatedMed)
        }
    }

    fun addMedication(name: String, dosage: String, time: String) {
        viewModelScope.launch {
            val med = MedicationEntity(name = name, dosage = dosage, time = time)
            medicationDao.insertMedication(med)
        }
    }
}
