package com.example.myapplication_ass2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MessageViewModel(application: Application) : AndroidViewModel(application) {
    // 构建数据库实例，添加 fallbackToDestructiveMigration() 消除版本升级问题
    private val database = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "eldercare_database"
    )
        .fallbackToDestructiveMigration()
        .build()

    private val messageDao = database.messageDao()

    private val _messages = MutableStateFlow<List<MessageEntity>>(emptyList())
    val messages: StateFlow<List<MessageEntity>> = _messages

    init {
        viewModelScope.launch {
            messageDao.getAllMessages().collect { msgs ->
                _messages.value = msgs
            }
        }
    }

    fun addMessage(sender: String, text: String) {
        viewModelScope.launch {
            val message = MessageEntity(sender = sender, text = text)
            messageDao.insertMessage(message)
        }
    }
}
