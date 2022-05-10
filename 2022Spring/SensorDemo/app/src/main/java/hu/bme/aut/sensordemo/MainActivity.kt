package hu.bme.aut.sensordemo

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.aut.sensordemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener {

    lateinit var sensorManager: SensorManager
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        binding.btnSensorList.setOnClickListener {
            binding.tvStatus.text = ""
            sensorManager.getSensorList(Sensor.TYPE_ALL).forEach {
                binding.tvStatus.append("${it.name}\n")
            }
        }

        binding.btnStartSensor.setOnClickListener {
            //var sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
            //var sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
            var sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        //binding.tvStatus.text = "${event!!.values.get(0)}"

        var x = event?.values?.get(0)
        var y = event?.values?.get(1)
        var z = event?.values?.get(2)

        binding.tvStatus.text = "x: $x, y: $y, z: $z"
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onStop() {
        super.onStop()
        sensorManager.unregisterListener(this)
    }
}