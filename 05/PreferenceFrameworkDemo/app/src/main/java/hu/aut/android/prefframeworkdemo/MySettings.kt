package hu.aut.android.prefframeworkdemo

import android.os.Bundle
import android.preference.PreferenceFragment
import android.widget.Toast
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.preference.PreferenceActivity


class MySettings : PreferenceActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        PreferenceManager.getDefaultSharedPreferences(this).
        registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences, key: String) {
        Toast.makeText(this, key, Toast.LENGTH_LONG).show()
    }

    override fun isValidFragment(fragmentName: String): Boolean {
        return fragmentName == "hu.aut.android.prefframeworkdemo.MySettings\$FragmentSettingsBasic"
        //return true;
    }

    override fun onBuildHeaders(target: List<PreferenceActivity.Header>) {
        loadHeadersFromResource(R.xml.settings_headers, target)
    }


    class FragmentSettingsBasic : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.mainsettings)
        }
    }
}
