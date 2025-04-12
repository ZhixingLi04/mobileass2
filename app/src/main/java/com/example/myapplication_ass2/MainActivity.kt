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
// 注意：删除错误的 Message 导入
// import androidx.datastore.core.Message
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

            // 不再需要 chatMessages（FamilyCommunicationScreen 内部用 MessageViewModel 加载数据）
            // 例如：val chatMessages = remember { mutableStateListOf(...)} // 删除该变量

            // checkedStates 此变量之前用于 MedicationScreen，不再传递给 MedicationScreen
            val checkedStates = remember { mutableStateListOf(false, false, false, false) }
            // 将任务列表类型修改为 RoutineTaskEntity（不是 RoutineTask）
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
    // 注意：routineTasks 类型为 RoutineTaskEntity
    routineTasks: MutableList<RoutineTaskEntity>,
    doctorRoutineTasks: MutableList<String>,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController, settingsViewModel)
        }
        composable("settings") {
            SettingsScreen(navController, settingsViewModel)
        }
        composable("medication") {
            // 只传入 navController，MedicationScreen 内部通过 viewModel 获取 MedicationViewModel
            MedicationScreen(navController)
        }
        composable("daily_routine") {
            // 修改为不传入额外任务列表，使用 DailyRoutineScreen 的默认 viewModel 获取数据
            DailyRoutineScreen(navController)
        }
        composable("appointments") {
            // 同样用 DailyRoutineScreen 作为占位屏，不传入 doctorRoutineTasks
            DailyRoutineScreen(navController)
        }
        composable("communication") {
            // FamilyCommunicationScreen 内部通过 MessageViewModel 获取消息
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

            val options = listOf(
                Triple("Medication Schedule", "medication", Icons.Default.MedicalServices),
                Triple("Daily Routine Schedule", "daily_routine", Icons.Default.List),
                Triple("Appointments List", "appointments", Icons.Default.DateRange),
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
