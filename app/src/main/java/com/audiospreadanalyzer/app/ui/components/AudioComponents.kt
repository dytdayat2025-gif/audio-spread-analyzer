package com.audiospreadanalyzer.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AudioLevelCard(
    currentDb: Float,
    averageDb: Float,
    maxDb: Float,
    minDb: Float,
    totalMeasurements: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Level Suara Saat Ini",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Light
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = String.format("%.1f", currentDb),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6200EE)
            )
            
            Text(
                text = "dB",
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem("Rata-rata", averageDb)
                StatItem("Maksimum", maxDb)
                StatItem("Minimum", minDb)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Total Pengukuran: $totalMeasurements",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun StatItem(label: String, value: Float) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = String.format("%.1f", value),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6200EE)
        )
        Text(
            text = "dB",
            fontSize = 10.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun LocationInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Lokasi/Posisi") },
        placeholder = { Text("Contoh: Depan Speaker, Sudut Ruangan") },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        singleLine = true,
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
fun RecordingButton(
    isRecording: Boolean,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = if (isRecording) onStopRecording else onStartRecording,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isRecording) Color(0xFFF44336) else Color(0xFF4CAF50)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = if (isRecording) "Hentikan Pengukuran" else "Mulai Pengukuran",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF6200EE)
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun MeasurementsList(
    measurements: List<com.audiospreadanalyzer.app.data.model.AudioMeasurement>,
    modifier: Modifier = Modifier
) {
    if (measurements.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Belum ada pengukuran",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            measurements.forEachIndexed { index, measurement ->
                MeasurementItem(
                    index = index + 1,
                    location = measurement.location,
                    level = measurement.levelDb,
                    timestamp = measurement.timestamp.toString()
                )
            }
        }
    }
}

@Composable
fun MeasurementItem(
    index: Int,
    location: String,
    level: Float,
    timestamp: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "#$index - $location",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = timestamp,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            
            Text(
                text = String.format("%.1f dB", level),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = when {
                    level < 50 -> Color(0xFF4CAF50) // Hijau
                    level < 85 -> Color(0xFFFF9800) // Orange
                    else -> Color(0xFFF44336) // Merah
                }
            )
        }
    }
}
