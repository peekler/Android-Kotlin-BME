package hu.bme.aut.chartdemo

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import hu.bme.aut.chartdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnChartValueSelectedListener {

    private var income = 100000
    private var expense = 50000

    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.chartBalance.setEntryLabelTextSize(12f)

        updateChart()

        binding.btnIncome.setOnClickListener {
            income += binding.etMoney.text.toString().toInt()
            updateChart()
        }

        binding.btnExpense.setOnClickListener {
            expense += binding.etMoney.text.toString().toInt()
            updateChart()
        }

        binding.chartBalance.setOnChartValueSelectedListener(this)
    }

    private fun updateChart() {
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(income.toFloat(), "Income"))
        entries.add(PieEntry(expense.toFloat(), "Expense"))

        val dataSet = PieDataSet(entries, "Balance")

        dataSet.setDrawIcons(false)

        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        val colors = ArrayList<Int>()
        colors.add(Color.GREEN)
        colors.add(Color.RED)

        /*for (c in ColorTemplate.PASTEL_COLORS) {
            colors.add(c)
        }*/
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLUE)

        binding.chartBalance.data = data

        binding.chartBalance.highlightValues(null)

        binding.chartBalance.invalidate()
    }

    override fun onNothingSelected() {

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }
}
