package hu.bme.sensordemo

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.sensordemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener {

    lateinit var binding: ActivityMainBinding
    lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        binding.btnListSensors.setOnClickListener {
            listAllSensors()
        }

        binding.btnStartSensor.setOnClickListener {
            //var sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
            var sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun listAllSensors() {
        binding.tvStatus.text = ""
        sensorManager.getSensorList(Sensor.TYPE_ALL).forEach {
            binding.tvStatus.append("${it.name}\n")
        }

    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        var x = sensorEvent?.values?.get(0)
        var y = sensorEvent?.values?.get(1)
        var z = sensorEvent?.values?.get(2)

        binding.tvStatus.text = "x: $x y: $y z: $z"
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}