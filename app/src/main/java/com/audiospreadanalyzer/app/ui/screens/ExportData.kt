package com.audiospreadanalyzer.app.ui.screens

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.audiospreadanalyzer.app.BuildConfig
import java.io.File

fun exportDataAsFile(context: Context, csvContent: String) {
    val fileName = "audio_measurements_${System.currentTimeMillis()}.csv"
    val file = File(context.cacheDir, fileName)
    file.writeText(csvContent)
    val uri = FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.fileprovider", file)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/csv"
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_SUBJECT, "Audio Spread Analyzer - Data Pengukuran")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Bagikan File CSV"))
}
