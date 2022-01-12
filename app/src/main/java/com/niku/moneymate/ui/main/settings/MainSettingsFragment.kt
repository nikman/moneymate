package com.niku.moneymate.ui.main.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.niku.moneymate.R

private const val TAG = "MainSettingsFragment"

class MainSettingsFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey)
    }

    /*fun interface Callbacks {
        fun onLoadFromCSVClick()
    }

    private lateinit var binding: MainSettingsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater,
                R.layout.main_settings_fragment,
                container,
            false)
        //initInstances(savedInstanceState)
        binding.callbacks = Callbacks {

            Log.d(TAG, "onClick")

            val intent = Intent()
                .setType("*/   /*")
                .setAction(Intent.ACTION_GET_CONTENT)

            val act: Activity = requireActivity()

            ActivityCompat.startActivityForResult(
                act, Intent.createChooser(intent, "Select a file"), 111,
                null) }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 111) {
            Log.d(TAG, data.toString())
        }
    }*/

    companion object {
        fun newBundle(): Bundle {
            return Bundle()
        }
    }

}