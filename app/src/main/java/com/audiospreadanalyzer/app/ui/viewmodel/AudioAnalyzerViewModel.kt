package com.audiospreadanalyzer.app.ui.viewmodel

import android.media.AudioRecord
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.audiospreadanalyzer.app.data.model.AudioMeasurement
import com.audiospreadanalyzer.app.data.model.AudioStats
import com.audiospreadanalyzer.app.data.repository.AudioRepository
import com.audiospreadanalyzer.app.domain.usecase.RecordAudioUseCase
import com.audiospreadanalyzer.app.domain.usecase.GetAudioStatsUseCase
import com.audiospreadanalyzer.app.domain.usecase.ExportDataUseCase
import com.audiospreadanalyzer.app.util.AudioAnalyzer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class AudioAnalyzerViewModel : ViewModel() {
    private val repository = AudioRepository()
    private val recordAudioUseCase = RecordAudioUseCase(repository)
    private val getAudioStatsUseCase = GetAudioStatsUseCase(repository)
    private val exportDataUseCase = ExportDataUseCase(repository)
    
    private var audioRecord: AudioRecord? = null
    private var recordingJob: Job? = null
    
    private val _audioStats = MutableStateFlow(AudioStats())
    val audioStats: StateFlow<AudioStats> = _audioStats.asStateFlow()
    
    private val _measurements = MutableStateFlow<List<AudioMeasurement>>(emptyList())
    val measurements: StateFlow<List<AudioMeasurement>> = _measurements.asStateFlow()
    
    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()
    
    private val _currentLocation = MutableStateFlow("Titik 1")
    val currentLocation: StateFlow<String> = _currentLocation.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    init {
        observeStats()
    }
    
    private fun observeStats() {
        viewModelScope.launch {
            getAudioStatsUseCase.getStats().collect { stats ->
                _audioStats.value = stats
            }
        }
        
        viewModelScope.launch {
            getAudioStatsUseCase.getMeasurements().collect { measurements ->
                _measurements.value = measurements
            }
        }
    }
    
    fun startRecording() {
        if (_isRecording.value) return
        
        audioRecord = AudioAnalyzer.getAudioRecorder()
        if (audioRecord == null) {
            _errorMessage.value = "Microphone tidak tersedia"
            return
        }
        
        audioRecord?.startRecording()
        _isRecording.value = true
        
        recordingJob = viewModelScope.launch(Dispatchers.Default) {
            val buffer = ShortArray(1024)
            while (isActive && _isRecording.value) {
                val readSize = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                if (readSize > 0) {
                    recordAudioUseCase.recordAudio(
                        buffer.sliceArray(0 until readSize),
                        _currentLocation.value
                    )
                }
            }
        }
    }
    
    fun stopRecording() {
        if (!_isRecording.value) return
        
        _isRecording.value = false
        recordingJob?.cancel()
        
        try {
            audioRecord?.stop()
            audioRecord?.release()
            audioRecord = null
        } catch (e: Exception) {
            _errorMessage.value = e.message
        }
    }
    
    fun setCurrentLocation(location: String) {
        _currentLocation.value = location
    }
    
    fun clearMeasurements() {
        stopRecording()
        repository.clearMeasurements()
        _errorMessage.value = null
    }
    
    fun exportAsCSV(): String {
        return exportDataUseCase.exportAsCsv(_measurements.value)
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
    
    override fun onCleared() {
        super.onCleared()
        stopRecording()
    }
}
