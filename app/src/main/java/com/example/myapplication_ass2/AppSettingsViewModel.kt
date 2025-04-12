package com.example.myapplication_ass2

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AppSettingsViewModel : ViewModel() {
    var fontSize = mutableStateOf(16f)
}
