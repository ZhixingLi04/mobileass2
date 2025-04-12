package com.example.myapplication_ass2

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineTaskDao {
    @Query("SELECT * FROM routine_task_table")
    fun getAllTasks(): Flow<List<RoutineTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: RoutineTaskEntity)

    @Delete
    suspend fun deleteTask(task: RoutineTaskEntity)
}
