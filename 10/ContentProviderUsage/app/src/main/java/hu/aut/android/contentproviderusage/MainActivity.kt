package hu.aut.android.contentproviderusage

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.provider.CalendarContract
import android.content.ContentValues
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        val KEY_LOG = "LOG_PROVIDER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()

        requestNeededPermission()
    }

    private fun initUI() {
        btnGet.setOnClickListener {
            val cursorContacts = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER),
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE '%Tamás%'",
                    //null,
                    null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " DESC")

            //Toast.makeText(MainActivity.this, ""+c.getCount(), Toast.LENGTH_LONG).show();

            while (cursorContacts.moveToNext()) {
                val name = cursorContacts.getString(cursorContacts.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                Log.d(KEY_LOG, name)
                Toast.makeText(this@MainActivity, name, Toast.LENGTH_LONG).show()
            }
        }
        btnInsert.setOnClickListener {
            try {
                val values = ContentValues()
                values.put(CalendarContract.Events.DTSTART, System.currentTimeMillis())
                values.put(CalendarContract.Events.DTEND, System.currentTimeMillis() + 60000)
                values.put(CalendarContract.Events.TITLE, "Vége")
                values.put(CalendarContract.Events.DESCRIPTION, "Legyen már vége az órának")
                values.put(CalendarContract.Events.CALENDAR_ID, 1)
                values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID())

                val uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)

                //contentResolver.delete(CalendarContract.Events.CONTENT_URI, CalendarContract.Events._ID+"=599", null)

                Log.d(KEY_LOG, uri.toString())
                //showCallLog();
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CALENDAR),
                    101)
        } else {
            // we are ok
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            101 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@MainActivity, "Permissions granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity,
                            "Permissions are NOT granted", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}
