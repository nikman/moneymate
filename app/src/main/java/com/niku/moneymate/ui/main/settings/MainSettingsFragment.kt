package com.niku.moneymate.ui.main.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.internal.ContextUtils
import com.niku.moneymate.R
import com.niku.moneymate.databinding.MainSettingsFragmentBinding
import java.util.*

private const val TAG = "MainSettingsFragment"

class MainSettingsFragment: Fragment() {

    fun interface Callbacks {
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
                .setType("*/*")
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
    }

    companion object {
        fun newBundle(): Bundle {
            return Bundle()
        }
    }

}