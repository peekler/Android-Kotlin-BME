package hu.ait.viewdemo


import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import hu.ait.viewdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //private val cityNames = listOf("New York", "New Delhi", "New Orleans",
    //  "Budapest", "Bukarest")

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.myWebView.loadUrl("https://webuni.hu/kepzesek")
        binding.myWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView,
                                                  request: WebResourceRequest
            ): Boolean {
                binding.myWebView.loadUrl(request.url.toString())
                return true
            }
        }

        val cityAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            resources.getStringArray(R.array.cities)
        )
        binding.autoCompleteCities.setAdapter(cityAdapter)
        binding.autoCompleteCities.threshold = 1


        binding.rbtnRed.setOnClickListener {
            binding.root.setBackgroundColor(Color.RED)
        }
        binding.rbtnGreen.setOnClickListener {
            binding.root.setBackgroundColor(Color.GREEN)
        }
        binding.rbtnWhite.setOnClickListener {
            binding.root.setBackgroundColor(Color.WHITE)
        }

        val fruitsAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.fruites,
            android.R.layout.simple_spinner_item
        )
        fruitsAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        binding.spinnerFruits.adapter = fruitsAdapter



        binding.spinnerFruits.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Toast.makeText(this@MainActivity,
                    binding.spinnerFruits.selectedItem.toString(),
                    Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    override fun onBackPressed() {
        if (binding.myWebView.canGoBack()) {
            binding.myWebView.goBack()
        } else {
            super.onBackPressed()
        }
    }

}