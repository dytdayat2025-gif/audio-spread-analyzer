package com.audiospreadanalyzer.app.domain.usecase

import com.audiospreadanalyzer.app.data.model.AudioMeasurement
import com.audiospreadanalyzer.app.data.repository.AudioRepository
import com.audiospreadanalyzer.app.util.AudioAnalyzer
import kotlinx.coroutines.flow.StateFlow

class RecordAudioUseCase(private val repository: AudioRepository) {
    fun recordAudio(audioData: ShortArray, location: String): AudioMeasurement? {
        val db = AudioAnalyzer.calculateDecibels(audioData)
        if (db < 0 || db > 120) return null
        
        val measurement = AudioMeasurement(
            levelDb = db,
            location = location,
            duration = audioData.size.toLong()
        )
        
        repository.addMeasurement(measurement)
        return measurement
    }
}

class GetAudioStatsUseCase(private val repository: AudioRepository) {
    fun getStats() = repository.audioStats
    fun getMeasurements() = repository.measurements
}

class SaveMeasurementSessionUseCase(private val repository: AudioRepository) {
    fun execute(sessionName: String, measurements: List<AudioMeasurement>) {
        if (measurements.isEmpty()) return
        
        val avgDb = measurements.map { it.levelDb }.average().toFloat()
        val maxDb = measurements.maxOf { it.levelDb }
        val minDb = measurements.minOf { it.levelDb }
        
        val session = com.audiospreadanalyzer.app.data.model.MeasurementSession(
            sessionName = sessionName,
            measurements = measurements,
            averageDb = avgDb,
            maxDb = maxDb,
            minDb = minDb
        )
        
        repository.saveMeasurementSession(session)
    }
}

class ExportDataUseCase(private val repository: AudioRepository) {
    fun exportAsCsv(measurements: List<AudioMeasurement>): String {
        val header = "No,Location,Level (dB),Category,Timestamp\n"
        val rows = measurements.mapIndexed { index, measurement ->
            val category = AudioAnalyzer.getAudioLevelCategory(measurement.levelDb)
            "${index + 1},${measurement.location},${String.format("%.2f", measurement.levelDb)},$category,${measurement.timestamp}\n"
        }
        return header + rows.joinToString("")
    }
}
