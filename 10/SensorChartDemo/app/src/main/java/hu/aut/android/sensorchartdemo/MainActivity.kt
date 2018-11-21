package hu.aut.android.sensorchartdemo

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var sensorMonitored: Sensor

    private var lastUpdateTime = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        tvSensor.setOnClickListener {
            val sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL)
            tvSensor.text = ""
            for (sensor in sensorList) {
                tvSensor.append(sensor.name)
                tvSensor.append("\n")
            }
        }

        initChart()

        initSensorMonitoring()
    }

    private fun initSensorMonitoring() {
        sensorMonitored = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (sensorMonitored != null) {
            sensorManager.registerListener(this, sensorMonitored,
                    SensorManager.SENSOR_DELAY_GAME)
        }
    }

    private fun addEntry(event: SensorEvent) {
        if (chartSensor.data != null) {

            var set: ILineDataSet? = chartSensor.data.getDataSetByIndex(0)
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet()
                chartSensor.data.addDataSet(set)
            }

            chartSensor.data.addEntry(Entry(set.entryCount.toFloat(),
                event.values[0] + 5), 0)
            chartSensor.data.notifyDataChanged()

            chartSensor.notifyDataSetChanged()

            // limit the number of visible entries
            chartSensor.setVisibleXRangeMaximum(150F)
            // chartSensor.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            chartSensor.moveViewToX(chartSensor.data.getEntryCount().toFloat())
        }
    }

    private fun createSet(): LineDataSet {
        val set = LineDataSet(null, "Sensor Data")
        set.axisDependency = YAxis.AxisDependency.LEFT
        set.lineWidth = 3f
        set.color = Color.BLACK
        set.isHighlightEnabled = false
        set.setDrawValues(false)
        set.setDrawCircles(false)
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.cubicIntensity = 0.2f
        return set
    }

    private fun initChart() {
        chartSensor.description.isEnabled = true
        chartSensor.setTouchEnabled(true)

        chartSensor.isDragEnabled = true
        chartSensor.setScaleEnabled(true)
        chartSensor.setDrawGridBackground(false)

        chartSensor.setPinchZoom(true)
        chartSensor.setBackgroundColor(Color.argb(255, 255,252, 198))

        val data = LineData()
        data.setValueTextColor(Color.BLACK)
        chartSensor.data = data

        val l = chartSensor.legend
        l.form = Legend.LegendForm.LINE
        l.textColor = Color.BLACK

        val xl = chartSensor.xAxis
        xl.textColor = Color.BLACK
        xl.setDrawGridLines(false)
        xl.setAvoidFirstLastClipping(true)
        xl.labelCount = 0
        xl.isEnabled = true

        val leftAxis = chartSensor.axisLeft
        leftAxis.textColor = Color.BLACK
        leftAxis.setDrawGridLines(false)
        leftAxis.axisMaximum = 10f
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawGridLines(true)

        val rightAxis = chartSensor.axisRight
        rightAxis.isEnabled = false

        chartSensor.axisLeft.setDrawGridLines(false)
        chartSensor.xAxis.setDrawGridLines(false)
        chartSensor.setDrawBorders(false)
    }



    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensorMonitored, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this@MainActivity)

        super.onDestroy()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        val current = System.currentTimeMillis()
        if (current - lastUpdateTime > 10) {
            // chart frissítése
            addEntry(event)

            lastUpdateTime = current
        }
    }
}
