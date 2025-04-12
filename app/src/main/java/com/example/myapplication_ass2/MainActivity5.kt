package com.example.myapplication_ass2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication_ass2.ui.theme.MyApplication_ass2Theme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send

class MainActivity5 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplication_ass2Theme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FamilyCommunicationScreen(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun FamilyCommunicationScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // 通过 Room 加载消息记录
    val messageViewModel: MessageViewModel = viewModel()
    val messages by messageViewModel.messages.collectAsStateWithLifecycle(emptyList())
    var inputText by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Family Communication",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 12.dp)
        )

        Divider(color = Color.Gray.copy(alpha = 0.3f))
        Spacer(modifier = Modifier.height(8.dp))

        // 使用 LazyColumn 展示消息，reverseLayout 实现最新消息在上方
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            reverseLayout = true
        ) {
            items(messages) { message ->
                ChatBubble(
                    sender = message.sender,
                    text = message.text
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = inputText,
                onValueChange = { inputText = it },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFF0F0F0))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    val text = inputText.text.trim()
                    if (text.isNotEmpty()) {
                        // 通过 ViewModel 添加新消息，存入 Room 数据库中
                        messageViewModel.addMessage("Me", text)
                        inputText = TextFieldValue("")
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(24.dp))
            ) {
                Icon(
                    imageVector = Icons.Filled.Send, // 修复此处
                    contentDescription = "Send",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Back to Home", fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
fun ChatBubble(sender: String, text: String) {
    val isMe = sender == "Me"
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (isMe) Color(0xFFBBDEFB)
                    else MaterialTheme.colorScheme.surfaceVariant
                )
                .padding(12.dp)
        ) {
            if (!isMe) {
                Text(
                    text = sender,
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
