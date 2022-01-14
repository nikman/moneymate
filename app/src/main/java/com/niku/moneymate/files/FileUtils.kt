package com.niku.moneymate.files

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.google.android.material.internal.ContextUtils.getActivity
import java.io.File

private const val TAG = "FileUtils"

class FileUtils {

    fun Context.launchFileIntent(filePath: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = FileProvider.getUriForFile(this, packageName, File(filePath))
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivity(Intent.createChooser(intent, "Select Application"))
    }

    fun readFileLineByLine(fileName: String)
        = File(fileName).forEachLine { println(it) }

    fun getAccountsDataFromFile(text: String) {
        for (line in text.lines()) {
            // string.removePrefix("Кот")
            // string.removeSuffix("себе")
            /*string.removeSurrounding(
                "та" // delimiter
            )*/
        }
    }

}