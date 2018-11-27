package hu.aut.android.incomeexpensepiechart

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnChartValueSelectedListener {

    private var income = 100000
    private var expense = 50000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chartBalance.setEntryLabelTextSize(12f)

        updateChart()

        btnIncome.setOnClickListener {
            if (!TextUtils.isEmpty(etMoney.text.toString())) {
                income += etMoney.text.toString().toInt()
                updateChart()
            }
        }

        btnExpense.setOnClickListener {
            if (!TextUtils.isEmpty(etMoney.text.toString())) {
                expense += etMoney.text.toString().toInt()
                updateChart()
            }
        }

        chartBalance.setOnChartValueSelectedListener(this)
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

        chartBalance.data = data

        chartBalance.highlightValues(null)

        chartBalance.invalidate()
    }

    override fun onNothingSelected() {

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }
}
