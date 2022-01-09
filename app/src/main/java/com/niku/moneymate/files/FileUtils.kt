package com.niku.moneymate.files

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import java.io.File

class FileUtils {

    fun Context.launchFileIntent(filePath: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = FileProvider.getUriForFile(this, packageName, File(filePath))
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivity(Intent.createChooser(intent, "Select Application"))
    }

    fun readFileLineByLine(fileName: String)
        = File(fileName).forEachLine { println(it) }

}