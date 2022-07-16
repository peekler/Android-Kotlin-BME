package hu.bme.aut.archcomponentsdemo

import android.hardware.SensorEvent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val acceleroSensorViewModel: AcceleroSensorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1.
        //lifecycle.addObserver(SensorLifeCycleObserver(this))

        // 2.
//        val acceleroLiveData = AcceleroSensorLiveData(this)
//
//        acceleroLiveData.observe(this,
//            object: Observer<SensorEvent>{
//                override fun onChanged(t: SensorEvent?) {
//                    tvData.text = "X: ${t!!.values[0]} Y: ${t!!.values[1]} Z: ${t!!.values[2]}"
//                }
//            }
//        )

        // 3.

        acceleroSensorViewModel.getAcceleroLiveData(this)?.observe(this,
                object: Observer<SensorEvent>{
                    override fun onChanged(t: SensorEvent?) {
                        tvData.text = "X: ${t!!.values[0]} Y: ${t!!.values[1]} Z: ${t!!.values[2]}"
                    }
                }
        )
    }
}