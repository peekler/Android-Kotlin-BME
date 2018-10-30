package hu.aut.android.camerademo

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.livinglifetechway.quickpermissions.annotations.WithPermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPhoto.setOnClickListener {
            takePhoto()
        }
    }

    @WithPermissions(
        permissions = [Manifest.permission.CAMERA]
    )
    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            takePhotoIntent ->
            takePhotoIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePhotoIntent, 1001)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            data?.also {
                val imageBitmap = it.extras.get("data") as Bitmap
                ivPhoto.setImageBitmap(imageBitmap)
            }
        }
    }

}
