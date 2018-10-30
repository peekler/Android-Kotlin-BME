package hu.aut.android.qrcodedemozxing

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.zxing.Result
import com.livinglifetechway.quickpermissions.annotations.WithPermissions
import kotlinx.android.synthetic.main.activity_main.*
import me.dm7.barcodescanner.zxing.ZXingScannerView

class MainActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        setContentView(R.layout.activity_main)
    }

    public override fun onResume() {
        super.onResume()
        startCamera()
    }


    @WithPermissions(
        permissions = [Manifest.permission.CAMERA]
    )
    private fun startCamera() {
        zxingView.setResultHandler(this) // Register ourselves as a handler for scan results.
        zxingView.startCamera()          // Start camera on resume
    }

    public override fun onPause() {
        super.onPause()
        zxingView.stopCamera()           // Stop camera on pause
    }

    override fun handleResult(rawResult: Result) {
        // Do something with the result here
        /*Log.v("QRDEMO", rawResult.getText()) // Prints scan results
        Log.v("QRDEMO",
            rawResult.getBarcodeFormat().toString()
        )*/ // Prints the scan format (qrcode, pdf417 etc.)

        tvScan.text = rawResult.text

        // If you would like to resume scanning, call this method below:
        zxingView.resumeCameraPreview(this)
    }
}
