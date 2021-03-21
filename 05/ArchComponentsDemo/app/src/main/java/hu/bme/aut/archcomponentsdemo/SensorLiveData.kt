package hu.bme.aut.archcomponentsdemo

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Toast
import androidx.lifecycle.LiveData

class AcceleroSensorLiveData(private val context: Context) : LiveData<SensorEvent>(),
    SensorEventListener {

    private val sensorManager: SensorManager
    private val acceleroSensor: Sensor

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        acceleroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onActive() {
        sensorManager.registerListener(
            this,
            acceleroSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onInactive() {
        sensorManager.unregisterListener(this)
        Toast.makeText(context, "INACTIVATE", Toast.LENGTH_SHORT).show()
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        value = sensorEvent
    }

    override fun onAccuracyChanged(sensor: Sensor, i: Int) {}
}