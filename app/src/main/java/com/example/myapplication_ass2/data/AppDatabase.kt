package com.example.myapplication_ass2

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MedicationEntity::class, RoutineTaskEntity::class, MessageEntity::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicationDao(): MedicationDao
    abstract fun routineTaskDao(): RoutineTaskDao
    abstract fun messageDao(): MessageDao
}
