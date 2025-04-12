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

    // Use Scaffold to fix the bottom button in the bottomBar
    Scaffold(
        bottomBar = {
            // Add appropriate padding in the outer layer
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
            // Background image
            Image(
                painter = painterResource(id = R.drawable.b),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.2f
            )

            // Wrap the main content with verticalScroll to allow scrolling when content overflows
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

                // Display medication information (success); iterate through the list and show details for each medication
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

                // Display error message (failure)
                error?.let {
                    Text(
                        text = "Error: $it",
                        color = Color.Red,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                // Add extra Spacer to ensure there is spacing between the bottom of the scrollable content and the bottomBar
                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }
}
