package com.audiospreadanalyzer.app.data.model

import androidx.annotation.Keep
import java.time.LocalDateTime

@Keep
data class AudioMeasurement(
    val id: Long = 0,
    val levelDb: Float = 0f,
    val location: String = "",
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val duration: Long = 0L,
    val notes: String = ""
)

@Keep
data class MeasurementSession(
    val id: Long = 0,
    val sessionName: String = "",
    val measurements: List<AudioMeasurement> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val averageDb: Float = 0f,
    val maxDb: Float = 0f,
    val minDb: Float = 0f
)

@Keep
data class AudioStats(
    val currentDb: Float = 0f,
    val averageDb: Float = 0f,
    val maxDb: Float = 0f,
    val minDb: Float = 0f,
    val totalMeasurements: Int = 0,
    val isRecording: Boolean = false
)
