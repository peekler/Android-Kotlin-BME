package hu.aut.android.prefframeworkdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.Menu
import android.view.MenuItem


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.getItemId()
        if (id == R.id.action_settings) {
            // itt fogjuk meghívni a saját SettingsActivity-nket
            val i = Intent(this, MySettings::class.java)
            i.putExtra(":android:no_headers", true)
            i.putExtra(":android:show_fragment", MySettings.FragmentSettingsBasic::class.java.getName())
            startActivity(i)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
