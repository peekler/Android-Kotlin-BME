package hu.ait.viewdemo

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var cityNames = arrayOf("New York", "New Delhi", "New Orleans", "Budapest", "Bukarest")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cityAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            cityNames
        )
        autoCompleteCities.setAdapter(cityAdapter)

        autoCompleteCities.threshold = 1


        rbtnWhite.setOnClickListener {
            layoutMain.setBackgroundColor(Color.WHITE)
        }
        rbtnRed.setOnClickListener {
            layoutMain.setBackgroundColor(Color.RED)
        }
        rbtnGreen.setOnClickListener {
            layoutMain.setBackgroundColor(Color.GREEN)
        }

        val fruitsAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.fruits_array,
            android.R.layout.simple_spinner_item
        )
        fruitsAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spinnerFruits.adapter = fruitsAdapter

        spinnerFruits.onItemSelectedListener = this

        myTimer.start()

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Toast.makeText(this, spinnerFruits.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}