package hu.bme.aut.architecturecomponentsdemo

import android.hardware.SensorEvent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import hu.bme.aut.architecturecomponentsdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val sensorViewModel: SensorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //1. LifeCycleObserver
        //lifecycle.addObserver(SensorLifeCycleObserver(this))




        //2. LiveData
/*        val acceleroLiveData = SensorLiveData(this)

        acceleroLiveData.observe(this,
            object : Observer<SensorEvent> {
                override fun onChanged(t: SensorEvent?) {
                    binding.tvData.text =
                        "X: ${t!!.values[0]} Y: ${t!!.values[1]} Z: ${t!!.values[2]}"
                }
            })*/

        //3. ViewModel
        sensorViewModel.getAcceleroLiveData(this)?.observe(this,
            object : Observer<SensorEvent> {
                override fun onChanged(t: SensorEvent?) {
                    binding.tvData.text =
                        "X: ${t!!.values[0]} Y: ${t!!.values[1]} Z: ${t!!.values[2]}"
                }
            })
    }


}