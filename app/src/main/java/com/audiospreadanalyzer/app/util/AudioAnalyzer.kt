package com.audiospreadanalyzer.app.util

import android.media.AudioRecord
import android.media.MediaRecorder
import kotlin.math.log10
import kotlin.math.sqrt

object AudioAnalyzer {
    private const val SAMPLE_RATE = 44100
    private const val CHANNEL_CONFIG = android.media.AudioFormat.CHANNEL_IN_MONO
    private const val AUDIO_FORMAT = android.media.AudioFormat.ENCODING_PCM_16BIT
    
    fun getAudioRecorder(): AudioRecord? {
        return try {
            val minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)
            AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                CHANNEL_CONFIG,
                AUDIO_FORMAT,
                minBufferSize * 2
            )
        } catch (e: Exception) {
            null
        }
    }
    
    fun calculateDecibels(audioData: ShortArray): Float {
        if (audioData.isEmpty()) return 0f
        
        // Calculate RMS (Root Mean Square)
        var sum = 0.0
        for (sample in audioData) {
            sum += (sample.toDouble() / 32768.0).let { it * it }
        }
        
        val rms = sqrt(sum / audioData.size)
        
        // Reference pressure for 94dB SPL at 1kHz
        val refPressure = 1.0
        
        // Convert to dB SPL
        val db = if (rms > 0) {
            20 * log10(rms / refPressure)
        } else {
            0f
        }
        
        // Clamp between 0 and 120 dB
        return db.coerceIn(0f, 120f)
    }
    
    fun getAudioLevelCategory(db: Float): String {
        return when {
            db < 30 -> "Senyap"
            db < 50 -> "Tenang"
            db < 70 -> "Normal"
            db < 85 -> "Keras"
            db < 100 -> "Sangat Keras"
            else -> "Berbahaya"
        }
    }
}
