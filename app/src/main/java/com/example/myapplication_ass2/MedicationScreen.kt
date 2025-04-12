package com.example.myapplication_ass2

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun MedicationScreen(
    navController: NavController,
    medicationViewModel: MedicationViewModel = viewModel()
) {
    // 从数据库中获取所有药物记录
    val medications by medicationViewModel.medications.collectAsStateWithLifecycle()
    // 控制添加药物对话框的显示状态
    var showDialog by remember { mutableStateOf(false) }

    // 将记录分为待服用和已服用
    val pendingMedications = medications.filter { !it.taken }
    val takenMedications = medications.filter { it.taken }

    Box(modifier = Modifier.fillMaxSize()) {
        // 背景图片
        Image(
            painter = painterResource(id = R.drawable.b),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.2f)
        )

        // 整体使用 LazyColumn 实现滑动布局，并区分两个部分
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 标题
            item {
                Text(
                    text = "Medication Schedule",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 待服用药物部分（显示“Done”按钮）
            if (pendingMedications.isNotEmpty()) {
                item {
                    Text(
                        text = "Pending Medications",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                items(pendingMedications) { medication ->
                    AnimatedVisibility(
                        visible = true,
                        exit = fadeOut(animationSpec = tween(500)) +
                                shrinkVertically(animationSpec = tween(500))
                    ) {
                        MedicationCard(
                            medication = medication,
                            onDone = { medicationViewModel.markAsDone(medication) }
                        )
                    }
                }
            }

            // 已服用药物部分（详细显示药物信息）
            if (takenMedications.isNotEmpty()) {
                item {
                    Text(
                        text = "Taken Medications",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                items(takenMedications) { medication ->
                    TakenMedicationCard(medication = medication)
                }
            }

            // “Back to Home” 按钮放在列表末尾
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        "Back to Home",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        // 页面右下角悬浮按钮，点击后显示添加药物对话框
        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Medication")
        }

        // 添加药物的对话框
        if (showDialog) {
            AddMedicationDialog(
                onAdd = { name, dosage, time ->
                    medicationViewModel.addMedication(name, dosage, time)
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Composable
fun AddMedicationDialog(
    onAdd: (name: String, dosage: String, time: String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Medication") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Medication Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = dosage,
                    onValueChange = { dosage = it },
                    label = { Text("Dosage") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && dosage.isNotBlank() && time.isNotBlank()) {
                        onAdd(name, dosage, time)
                        onDismiss()
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun MedicationCard(
    medication: MedicationEntity,
    onDone: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medication.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Dosage: ${medication.dosage}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Time: ${medication.time}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Button(
                onClick = onDone,
                modifier = Modifier.padding(start = 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Done")
            }
        }
    }
}

@Composable
fun TakenMedicationCard(medication: MedicationEntity) {
    // 卡片展示已服用药物的详细信息，不包含按钮
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = medication.name,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Dosage: ${medication.dosage}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Time: ${medication.time}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Status: Taken",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Green
            )
        }
    }
}
