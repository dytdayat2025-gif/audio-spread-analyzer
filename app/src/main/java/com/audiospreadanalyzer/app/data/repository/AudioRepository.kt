package com.audiospreadanalyzer.app.data.repository

import com.audiospreadanalyzer.app.data.model.AudioMeasurement
import com.audiospreadanalyzer.app.data.model.MeasurementSession
import com.audiospreadanalyzer.app.data.model.AudioStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AudioRepository {
    private val _audioStats = MutableStateFlow(AudioStats())
    val audioStats: StateFlow<AudioStats> = _audioStats
    
    private val _measurements = MutableStateFlow<List<AudioMeasurement>>(emptyList())
    val measurements: StateFlow<List<AudioMeasurement>> = _measurements
    
    private val sessions = mutableListOf<MeasurementSession>()
    
    fun addMeasurement(measurement: AudioMeasurement) {
        val currentList = _measurements.value.toMutableList()
        currentList.add(measurement)
        _measurements.value = currentList
        updateStats()
    }
    
    fun clearMeasurements() {
        _measurements.value = emptyList()
        _audioStats.value = AudioStats()
    }
    
    fun saveMeasurementSession(session: MeasurementSession) {
        sessions.add(session)
    }
    
    fun getSessions(): List<MeasurementSession> {
        return sessions
    }
    
    private fun updateStats() {
        val measurements = _measurements.value
        if (measurements.isEmpty()) {
            _audioStats.value = AudioStats()
            return
        }
        
        val dBValues = measurements.map { it.levelDb }
        val current = measurements.lastOrNull()?.levelDb ?: 0f
        val average = dBValues.average().toFloat()
        val max = dBValues.maxOrNull() ?: 0f
        val min = dBValues.minOrNull() ?: 0f
        
        _audioStats.value = AudioStats(
            currentDb = current,
            averageDb = average,
            maxDb = max,
            minDb = min,
            totalMeasurements = measurements.size,
            isRecording = false
        )
    }
}
