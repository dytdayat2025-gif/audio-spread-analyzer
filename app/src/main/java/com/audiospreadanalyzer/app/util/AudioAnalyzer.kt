package com.audiospreadanalyzer.app.util

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlin.math.log10
import kotlin.math.sqrt

object AudioAnalyzer {
    private const val SAMPLE_RATE = 44_100
    private const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
    private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT

    fun getAudioRecorder(): AudioRecord? = try {
        val minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)
        if (minBufferSize <= 0) return null
        AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLE_RATE,
            CHANNEL_CONFIG,
            AUDIO_FORMAT,
            minBufferSize * 2
        )
    } catch (_: Exception) {
        null
    }

    /** Returns a relative dBFS-derived level. Calibration is required for true dB SPL. */
    fun calculateDecibels(audioData: ShortArray): Float {
        if (audioData.isEmpty()) return 0f
        var sum = 0.0
        for (sample in audioData) {
            val normalized = sample.toDouble() / Short.MAX_VALUE
            sum += normalized * normalized
        }
        val rms = sqrt(sum / audioData.size)
        if (rms <= 0.0) return 0f
        // Offset keeps the UI in a practical 0..120 range; this is not calibrated SPL.
        return (20.0 * log10(rms) + 120.0).toFloat().coerceIn(0f, 120f)
    }

    fun getAudioLevelCategory(db: Float): String = when {
        db < 30f -> "Senyap"
        db < 50f -> "Tenang"
        db < 70f -> "Normal"
        db < 85f -> "Keras"
        db < 100f -> "Sangat Keras"
        else -> "Berbahaya"
    }
}
