package com.example.myapplication_ass2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routine_task_table")
data class RoutineTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val time: String
)
