package com.niku.moneymate.ui.main.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.core.app.ActivityCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import com.niku.moneymate.R
import java.util.concurrent.Executors

private const val TAG = "MainSettingsFragment"

class MainSettingsFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        if (preference != null) {
            return when (preference.key) {
                "load_from_csv" -> {
                    val intent = Intent()
                        .setType("*/*")
                        .setAction(Intent.ACTION_GET_CONTENT)

                    val act: Activity = requireActivity()

                    ActivityCompat.startActivityForResult(
                        act, Intent.createChooser(intent, "Select a file"), 111,
                        null)
                    true
                }
                "load_from_assets" -> {
                    com.niku.moneymate.files.FileUtils().readFileFromAssetsLineByLine(requireContext())
                    true
                }
                else -> {
                    super.onPreferenceTreeClick(preference)
                }
            }
        }
        return true
    }

    fun interface Callbacks {
        fun onLoadFromCSVClick()
    }

    @Composable
    fun SimpleComposable() {
        Text(
            text = "Under construction",
            color = androidx.compose.ui.graphics.Color.Magenta,
            fontSize = TextUnit.Unspecified,
            letterSpacing = TextUnit.Unspecified,
            overflow = TextOverflow.Ellipsis
        )
    }

    @Preview
    @Composable
    fun ComposablePreview() {
        SimpleComposable()
    }



    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 111) {
            Log.d(TAG, data.toString())
            val executor = Executors.newSingleThreadExecutor()
            executor.execute {
                com.niku.moneymate.files.FileUtils().readFileLineByLine(data.toString())
            }
        }
    }*/

    companion object {
        fun newBundle(): Bundle {
            return Bundle()
        }
    }

}