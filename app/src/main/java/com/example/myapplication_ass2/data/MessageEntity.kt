package com.example.myapplication_ass2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message_table")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sender: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
