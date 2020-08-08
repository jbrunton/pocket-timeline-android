package com.pocketlearningapps.timeline.ui.dev

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.pocketlearningapps.timeline.R

class DevSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.dev_settings, rootKey)
    }
}
