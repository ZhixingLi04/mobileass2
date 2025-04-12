package com.example.myapplication_ass2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RoutineTaskViewModel(application: Application) : AndroidViewModel(application) {
    // To build a database instance, add fallbackToDestructiveMigration () to solve the problem of version upgrade
    private val database = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "eldercare_database"
    )
        .fallbackToDestructiveMigration()
        .build()

    private val routineTaskDao = database.routineTaskDao()

    private val _tasks = MutableStateFlow<List<RoutineTaskEntity>>(emptyList())
    val tasks: StateFlow<List<RoutineTaskEntity>> = _tasks

    init {
        viewModelScope.launch {
            routineTaskDao.getAllTasks().collect { tasks ->
                _tasks.value = tasks
            }
        }
    }

    fun addTask(name: String, description: String, time: String) {
        viewModelScope.launch {
            val task = RoutineTaskEntity(name = name, description = description, time = time)
            routineTaskDao.insertTask(task)
        }
    }

    fun deleteTask(task: RoutineTaskEntity) {
        viewModelScope.launch {
            routineTaskDao.deleteTask(task)
        }
    }
}
