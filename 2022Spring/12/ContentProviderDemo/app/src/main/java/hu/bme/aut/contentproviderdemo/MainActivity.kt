package hu.bme.aut.contentproviderdemo

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import hu.bme.aut.contentproviderdemo.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 1001
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CALENDAR)
    }

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (allPermissionsGranted()) {
            setViews()
        } else {
            ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
                baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults:
            IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                setViews()
            } else {
                Toast.makeText(
                        this,
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun setViews() {
        binding.btnGet.isEnabled = true
        binding.btnInsert.isEnabled = true

        binding.btnGet.setOnClickListener {
            getContacts()
        }
        binding.btnInsert.setOnClickListener {
            insertCalendarEvent()
        }
    }

    private fun getContacts() {
        val cursorContacts = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER),
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE '%Tamás%'",
                //null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " DESC")

        Toast.makeText(this, ""+cursorContacts!!.count, Toast.LENGTH_LONG).show();

        while (cursorContacts!!.moveToNext()) {
            val name = cursorContacts.getString(cursorContacts.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            Toast.makeText(this@MainActivity, name, Toast.LENGTH_LONG).show()
        }
    }

    fun insertCalendarEvent() {
        try {
            val values = ContentValues()
            values.put(CalendarContract.Events.DTSTART, System.currentTimeMillis())
            values.put(CalendarContract.Events.DTEND, System.currentTimeMillis() + 60000)
            values.put(CalendarContract.Events.TITLE, "Demo")
            values.put(CalendarContract.Events.DESCRIPTION, "This is an event in my calenda :)")
            values.put(CalendarContract.Events.CALENDAR_ID, 1)
            values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID())

            val uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)

            //contentResolver.delete(CalendarContract.Events.CONTENT_URI, CalendarContract.Events._ID+"=599", null)

        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}