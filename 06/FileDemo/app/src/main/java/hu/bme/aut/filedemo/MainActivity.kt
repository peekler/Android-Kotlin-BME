package hu.bme.aut.filedemo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Binder
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import hu.bme.aut.filedemo.databinding.ActivityMainBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnWriteFile.setOnClickListener{
            writeFile("Hello! ${Date(System.currentTimeMillis()).toString()}");
        }

        binding.btnReadFile.setOnClickListener{
            binding.tvData.text = readFile()
        }
    }


    private fun writeFile(data: String) {
        lateinit var outputStream: FileOutputStream
        try {
            outputStream = openFileOutput("text.txt", Context.MODE_PRIVATE)
            outputStream.write(data.toByteArray())
            outputStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun readFile(): String {
        var result = ""
        lateinit var inputStream: FileInputStream
        try {
            inputStream = openFileInput("text.txt")
            result = inputStream.bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return result
    }
}
