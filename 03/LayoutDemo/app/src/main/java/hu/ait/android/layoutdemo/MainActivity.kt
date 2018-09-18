package hu.ait.android.layoutdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.empty_linear.*
import kotlinx.android.synthetic.main.layout_item.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.empty_linear)

        btnAdd.setOnClickListener {

            val item = layoutInflater.inflate(R.layout.layout_item,
                    null, false)

            item.cbItem.text = "HELLO"
            item.btnDelete.setOnClickListener {
                layoutContent.removeView(item)
            }

            layoutContent.addView(item)

        }
    }
}
