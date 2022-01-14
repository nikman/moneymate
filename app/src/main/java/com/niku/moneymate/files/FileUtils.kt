package com.niku.moneymate.files

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File

private const val TAG = "FileUtils"

class FileUtils {

    fun Context.launchFileIntent(filePath: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = FileProvider.getUriForFile(this, packageName, File(filePath))
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivity(Intent.createChooser(intent, "Select Application"))
    }

    object AssetsLoader {

        fun loadTextFromAsset(context: Context, file: String): String {
            return context.assets.open(file).bufferedReader().use { reader ->
                reader.readText()
            }
        }

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

    fun readFileFromAssetsLineByLine(context: Context) {
        val text = AssetsLoader.loadTextFromAsset(context, "db_backup")
        Log.d(TAG, text)
    }
}