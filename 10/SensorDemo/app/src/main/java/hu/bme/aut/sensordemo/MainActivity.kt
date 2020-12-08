package hu.bme.aut.sensordemo

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        btnSensorList.setOnClickListener {
            listAllSensors()
        }

        btnStartSensor.setOnClickListener {
            var sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun listAllSensors() {
        tvStatus.text = ""
        sensorManager.getSensorList(Sensor.TYPE_ALL).forEach {
            tvStatus.append("${it.name}\n")
        }

    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        var x = sensorEvent?.values?.get(0)
        var y = sensorEvent?.values?.get(1)
        var z = sensorEvent?.values?.get(2)

        tvStatus.text = "x: $x, y: $y, z: $z"
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}