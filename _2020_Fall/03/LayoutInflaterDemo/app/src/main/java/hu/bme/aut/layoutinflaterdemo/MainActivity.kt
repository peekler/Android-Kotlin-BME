package hu.bme.aut.layoutinflaterdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.data_row.view.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSave.setOnClickListener {
            addItem()
        }
    }

    private fun addItem() {
        val dataRowView = layoutInflater.inflate(R.layout.data_row,null, false)

        dataRowView.tvData.text = etData.text.toString()

        //layoutMain.addView(dataRowView)
    }
}