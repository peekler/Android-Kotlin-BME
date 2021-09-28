package hu.ait.layoutinflaterdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.todo_row.view.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSave.setOnClickListener {
            addTodoRow()
        }
    }

    private fun addTodoRow() {
        val todoRow = layoutInflater.inflate(R.layout.todo_row, null)

        todoRow.tvTodoText.text = etTodoText.text.toString()
        todoRow.btnDelete.setOnClickListener {
            layoutMain.removeView(todoRow)
        }

        layoutMain.addView(todoRow, 0)
    }
}