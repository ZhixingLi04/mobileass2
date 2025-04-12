package com.example.myapplication_ass2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController, settingsViewModel: AppSettingsViewModel) {
    var fontSize by remember { mutableStateOf(settingsViewModel.fontSize.value) }
    // Removed language-related variables
    var isDarkMode by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }

    // Set background and text colors based on dark mode state
    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val textColor = if (isDarkMode) Color.White else Color.Black

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title + Icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Settings",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Font size settings
        Text("Font Size Preview", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = textColor)
        Text("Aa", fontSize = fontSize.sp, color = textColor)

        Slider(
            value = fontSize,
            onValueChange = {
                fontSize = it
                settingsViewModel.fontSize.value = it
            },
            valueRange = 12f..30f,
            steps = 4,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Removed language selection section

        // Toggle settings items
        SettingToggleItem("Dark Mode", isDarkMode) { isDarkMode = it }
        Spacer(modifier = Modifier.height(12.dp))
        SettingToggleItem("Enable Notifications", notificationsEnabled) { notificationsEnabled = it }

        Spacer(modifier = Modifier.height(24.dp))

        // Removed Help Center button

        Spacer(modifier = Modifier.height(16.dp))

        Text("App Version: 1.0.0", fontSize = 14.sp, color = textColor)
        Text("Developed by Team ElderCare", fontSize = 14.sp, color = textColor)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate("communication") },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Call", color = MaterialTheme.colorScheme.onError)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Back to Home", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
fun SettingToggleItem(title: String, value: Boolean, onToggle: (Boolean) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontSize = 16.sp, modifier = Modifier.weight(1f))
            Switch(checked = value, onCheckedChange = onToggle)
        }
    }
}
