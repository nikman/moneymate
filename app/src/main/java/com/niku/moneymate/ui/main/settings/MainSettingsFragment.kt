package com.niku.moneymate.ui.main.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.niku.moneymate.R

private const val TAG = "MainSettingsFragment"

class MainSettingsFragment: PreferenceFragmentCompat() {

    private var callbacks: Callbacks? = null

    interface Callbacks {
        fun onCurrencyListFromPreferencesOpenSelected()
        fun onProjectListFromPreferencesOpenSelected()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
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
                "open_currency_list" -> {
                    callbacks?.onCurrencyListFromPreferencesOpenSelected()
                    true
                }
                "open_project_list" -> {
                    callbacks?.onProjectListFromPreferencesOpenSelected()
                    true
                }
                else -> {
                    super.onPreferenceTreeClick(preference)
                }
            }
        }
        return true
    }

    companion object {
        fun newBundle(): Bundle {
            return Bundle()
        }
    }

}