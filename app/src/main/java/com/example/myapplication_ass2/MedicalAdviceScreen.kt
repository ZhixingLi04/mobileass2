package com.example.myapplication_ass2

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun MedicalAdviceScreen(
    navController: NavController,
    medicalAdviceViewModel: MedicalAdviceViewModel = viewModel()
) {
    val focusManager = LocalFocusManager.current
    var drugName by remember { mutableStateOf("") }

    val drugInfo by medicalAdviceViewModel.drugInfo.collectAsState()
    val error by medicalAdviceViewModel.error.collectAsState()

    // 使用 Scaffold，将底部按钮固定在 bottomBar 中
    Scaffold(
        bottomBar = {
            // 外层加上适当的 padding
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Back to Home", fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 背景图片
            Image(
                painter = painterResource(id = R.drawable.b),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.2f
            )

            // 主体内容使用 verticalScroll 包裹，确保内容过多时能滚动查看
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Medical Advice",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = drugName,
                    onValueChange = { drugName = it },
                    label = { Text("Enter drug name") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default,
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (drugName.isNotBlank()) {
                            medicalAdviceViewModel.fetchDrugAdvice(drugName)
                            focusManager.clearFocus()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Search")
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 显示药物信息（成功），遍历列表并显示每个药物的详细信息
                drugInfo?.let { infos ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        infos.forEach { info ->
                            Text("Name: ${info.name}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Description: ${info.description}", fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Usage: ${info.usage}", fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Side Effects: ${info.side_effects.joinToString(", ")}", fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }

                // 显示错误信息（失败）
                error?.let {
                    Text(
                        text = "Error: $it",
                        color = Color.Red,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                // 添加额外的 Spacer，确保滚动内容下部与 bottomBar 有间距
                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }
}
