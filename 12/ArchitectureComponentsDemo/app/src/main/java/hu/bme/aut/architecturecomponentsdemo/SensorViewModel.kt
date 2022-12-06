package hu.bme.aut.architecturecomponentsdemo

import android.content.Context
import android.hardware.SensorEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class SensorViewModel : ViewModel() {
    private var acceleroSensorLiveData: SensorLiveData? = null

    init {
    }

    fun getAcceleroLiveData(context: Context) : LiveData<SensorEvent>? {
        if (acceleroSensorLiveData == null) {
            acceleroSensorLiveData = SensorLiveData(context)
        }
        return acceleroSensorLiveData
    }
}