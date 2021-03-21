package hu.bme.aut.archcomponentsdemo

import android.content.Context
import android.hardware.SensorEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class AcceleroSensorViewModel : ViewModel() {
    private var acceleroSensorLiveData: AcceleroSensorLiveData? = null

    fun getAcceleroLiveData(context: Context) : LiveData<SensorEvent>? {
        if (acceleroSensorLiveData == null) {
            acceleroSensorLiveData = AcceleroSensorLiveData(context)
        }
        return acceleroSensorLiveData
    }

}