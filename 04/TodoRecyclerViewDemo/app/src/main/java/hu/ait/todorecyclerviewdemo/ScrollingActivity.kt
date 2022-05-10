package hu.ait.todorecyclerviewdemo

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import hu.ait.todorecyclerviewdemo.adapter.TodoAdapter
import hu.ait.todorecyclerviewdemo.data.Todo
import kotlinx.android.synthetic.main.activity_scrolling.*
import java.util.*

class ScrollingActivity : AppCompatActivity() {

    lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            // create new item
            showAddTodoDialog()
        }

        todoAdapter = TodoAdapter(this)
        recyclerTodo.adapter = todoAdapter

        val itemDecoration = DividerItemDecoration(
            this, DividerItemDecoration.VERTICAL
        )
        recyclerTodo.addItemDecoration(itemDecoration)

        recyclerTodo.layoutManager = GridLayoutManager(
            this, 2)
    }


    fun showAddTodoDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Create new todo")

        val etTodoText = EditText(this)
        builder.setView(etTodoText)

        builder.setPositiveButton("Ok") { dialog, which ->
            todoAdapter.addTodo(
                Todo(
                    Date(System.currentTimeMillis()).toString(),
                    false,
                    etTodoText.text.toString()
                )
            )
        }

        builder.show()
    }

}
