package com.example.myapplication_ass2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.myapplication_ass2.ui.theme.MyApplication_ass2Theme
import com.example.myapplication_ass2.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val settingsViewModel: AppSettingsViewModel = viewModel()

            // checkedStates: This variable was previously used for MedicationScreen, but is no longer passed to MedicationScreen
            val checkedStates = remember { mutableStateListOf(false, false, false, false) }
            // Change the task list type to RoutineTaskEntity (not RoutineTask)
            val routineTasks = remember { mutableStateListOf<RoutineTaskEntity>() }
            val doctorRoutineTasks = remember {
                mutableStateListOf(
                    "07:00 - Wake up & light stretching",
                    "07:30 - Wash up & healthy breakfast",
                    "08:30 - Morning walk (outdoor or balcony)",
                    "10:00 - Reading / Puzzle / Brain exercise",
                    "12:00 - Healthy lunch (low salt, balanced)",
                    "13:00 - Nap (30–45 mins)",
                    "14:00 - Light activity (Tai Chi / gardening)",
                    "16:00 - Family video call / watch TV",
                    "18:00 - Dinner (light & easy to digest)",
                    "20:00 - Listen to music / reading",
                    "21:30 - Get ready for bed",
                    "22:00 - Sleep (consistent schedule)"
                )
            }

            MyApplication_ass2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHostContainer(
                        navController = navController,
                        settingsViewModel = settingsViewModel,
                        routineTasks = routineTasks,
                        doctorRoutineTasks = doctorRoutineTasks,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun NavHostContainer(
    navController: NavHostController,
    settingsViewModel: AppSettingsViewModel,
    routineTasks: MutableList<RoutineTaskEntity>,
    doctorRoutineTasks: MutableList<String>,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") {
            HomeScreen(navController, settingsViewModel)
        }
        composable("settings") {
            SettingsScreen(navController, settingsViewModel)
        }
        composable("medication") {
            // MedicationScreen obtains MedicationViewModel internally via viewModel
            MedicationScreen(navController)
        }
        composable("daily_routine") {
            // DailyRoutineScreen loads data using its internal ViewModel
            DailyRoutineScreen(navController)
        }
        // Added Medical Advice page (the original Appointments page has been removed)
        composable("medical_advice") {
            MedicalAdviceScreen(navController)
        }
        composable("communication") {
            FamilyCommunicationScreen(navController)
        }
    }
}

@Composable
fun HomeScreen(
    navController: NavHostController,
    settingsViewModel: AppSettingsViewModel
) {
    val fontSize = settingsViewModel.fontSize.value

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.a),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.5f
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.85f),
                            Color.White.copy(alpha = 0.4f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Elderly Care",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color(0xFF2D2D2D)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Welcome to Elderly Care!",
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Update the navigation menu: change "Appointments List" to "Medical Advice"
            val options = listOf(
                Triple("Medication Schedule", "medication", Icons.Default.MedicalServices),
                Triple("Daily Routine Schedule", "daily_routine", Icons.Default.List),
                Triple("Medical Advice", "medical_advice", Icons.Default.Info),
                Triple("Family Communication", "communication", Icons.Default.Phone),
                Triple("App Settings", "settings", Icons.Default.Settings)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                options.forEach { (text, route, icon) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(vertical = 10.dp)
                            .height(64.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        onClick = { navController.navigate(route) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(icon, contentDescription = null, tint = Color(0xFF3A3A3A))
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(
                                text = text,
                                fontSize = fontSize.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}
