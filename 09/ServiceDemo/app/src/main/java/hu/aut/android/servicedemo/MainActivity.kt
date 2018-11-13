package hu.aut.android.servicedemo

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.widget.Toast
import hu.aut.android.servicedemo.service.FloatingService
import hu.aut.android.servicedemo.service.ImageDownloadService
import hu.aut.android.servicedemo.service.TimeService
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val handler = object: Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.arg1 == Activity.RESULT_OK) {
                ivPhoto.setImageBitmap(msg.obj as Bitmap)
            }
        }
    }
    private var timeService : TimeService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intentService = Intent(this, TimeService::class.java)
        val intentTimeWindowService = Intent(
                this@MainActivity, FloatingService::class.java
        )

        btnStart.setOnClickListener {
            startService(intentService)
        }

        btnStop.setOnClickListener {
            stopService(intentService)
            stopService(intentTimeWindowService)
        }

        btnFloatService.setOnClickListener {
            startService(intentTimeWindowService)
        }

        val rand = Random(System.currentTimeMillis())

        btnGetImage.setOnClickListener {
            val intentImage = Intent(this@MainActivity, ImageDownloadService::class.java)

            intentImage.putExtra(ImageDownloadService.KEY_MESSENGER,
                    Messenger(handler))
            intentImage.putExtra(ImageDownloadService.KEY_IMAGE_URL,
                    "https://picsum.photos/200/200/?image=${rand.nextInt(1000)}")
            startService(intentImage)
        }

        btnSetTime.setOnClickListener {
            if (!TextUtils.isEmpty(etTime.text)) {
                timeService?.changeInterval(etTime.text.toString().toLong())
            } else {
                etTime.error = "This field can not be empty"
            }
        }

        requestOverlay()
    }

    private fun requestOverlay() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName"))
            startActivityForResult(intent, 1234)
        } else {
            requestNeededPermission()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1234) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    requestNeededPermission()
                }
            }
        }
    }

    private fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.FOREGROUND_SERVICE),
                    101)
        } else {
            // van már engedély
            bindService(
                    Intent(this, TimeService::class.java),
                    serviceConnection,
                    Context.BIND_AUTO_CREATE
            )
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName, serviceBinder: IBinder) {
            timeService = (serviceBinder as TimeService.TimeServiceBinder).service

            Toast.makeText(this@MainActivity, "Bind ready", Toast.LENGTH_LONG).show()

            btnSetTime.isEnabled = true
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            101 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@MainActivity, "Permissions granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity,
                            "Permissions are NOT granted", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    override fun onStop() {
        unbindService(serviceConnection)
        super.onStop()
    }
}
