package hu.bme.aut.recyclerviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.recyclerviewdemo.adapter.TodoAdapter
import hu.bme.aut.recyclerviewdemo.data.AppDatabase
import hu.bme.aut.recyclerviewdemo.data.Todo
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()

        btnAdd.setOnClickListener {
            thread {
                AppDatabase.getInstance(this).todoDao().insertTodo(
                    Todo(
                        null,
                        etTodo.text.toString(),
                        Date(System.currentTimeMillis()).toString(),
                        false
                    )
                )
            }
        }
    }

    private fun initRecyclerView() {
        todoAdapter = TodoAdapter(this)
        rwTodo.adapter = todoAdapter
        AppDatabase.getInstance(this).todoDao().getAllTodo().observe(this, { todos ->
            todoAdapter.submitList(todos)
        })
    }
}