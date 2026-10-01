package com.audiospreadanalyzer.app.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.GetApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.audiospreadanalyzer.app.ui.components.*
import com.audiospreadanalyzer.app.ui.viewmodel.AudioAnalyzerViewModel
import java.io.File

@Composable
fun MainScreen(viewModel: AudioAnalyzerViewModel = viewModel()) {
    val audioStats by viewModel.audioStats.collectAsState()
    val measurements by viewModel.measurements.collectAsState()
    val isRecording by viewModel.isRecording.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val context = LocalContext.current
    
    var showClearDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Audio Spread Analyzer") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Error Message
            if (errorMessage != null) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.errorContainer
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        IconButton(onClick = { viewModel.clearError() }) {
                            Icon(Icons.Default.Clear, contentDescription = "Close")
                        }
                    }
                }
            }
            
            // Audio Level Display Card
            AudioLevelCard(
                currentDb = audioStats.currentDb,
                averageDb = audioStats.averageDb,
                maxDb = audioStats.maxDb,
                minDb = audioStats.minDb,
                totalMeasurements = audioStats.totalMeasurements
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Location Input
            LocationInputField(
                value = currentLocation,
                onValueChange = { viewModel.setCurrentLocation(it) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Recording Button
            RecordingButton(
                isRecording = isRecording,
                onStartRecording = { viewModel.startRecording() },
                onStopRecording = { viewModel.stopRecording() }
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Action Buttons Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ActionButton(
                    text = "Ekspor",
                    onClick = {
                        exportDataAsFile(context, viewModel.exportAsCSV())
                    },
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.secondary
                )
                
                ActionButton(
                    text = "Hapus",
                    onClick = { showClearDialog = true },
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.error
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Measurements List Title
            Text(
                text = "Riwayat Pengukuran",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Measurements List
            MeasurementsList(measurements)
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
    
    // Clear Confirmation Dialog
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Hapus Semua Data?") },
            text = { Text("Tindakan ini tidak dapat dibatalkan.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearMeasurements()
                        showClearDialog = false
                    }
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

private fun exportDataAsFile(context: Context, csvContent: String) {
    try {
        val fileName = "audio_measurements_${System.currentTimeMillis()}.csv"
        val file = File(context.cacheDir, fileName)
        file.writeText(csvContent)
        
        val uri = Uri.fromFile(file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "Audio Spread Analyzer - Data Pengukuran")
        }
        context.startActivity(Intent.createChooser(intent, "Bagikan File CSV"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
