package com.example.myapplication_ass2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication_ass2.di.DrugContainer
import com.example.myapplication_ass2.network.DrugInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MedicalAdviceViewModel : ViewModel() {

    private val repository = DrugContainer.repository

    // 修改 _drugInfo 类型为 List<DrugInfo>?，初始值 null
    private val _drugInfo = MutableStateFlow<List<DrugInfo>?>(null)
    val drugInfo: StateFlow<List<DrugInfo>?> = _drugInfo

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchDrugAdvice(name: String) {
        viewModelScope.launch {
            val result = repository.fetchDrugInfo(name)
            result.onSuccess {
                _drugInfo.value = it
                _error.value = null
            }.onFailure {
                _drugInfo.value = null
                _error.value = "Failed to fetch: ${it.message}"
            }
        }
    }
}
