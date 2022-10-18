package hu.ait.todorecyclerview

import android.os.Bundle
import android.preference.PreferenceManager
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import hu.ait.todorecyclerview.adapter.TodoAdapter
import hu.ait.todorecyclerview.data.AppDatabase
import hu.ait.todorecyclerview.data.Todo
import hu.ait.todorecyclerview.touch.TouchCallback
import kotlinx.android.synthetic.main.activity_scrolling.*
import java.util.*
import kotlin.concurrent.thread

import androidx.lifecycle.Observer
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt

class ScrollingActivity : AppCompatActivity(), TodoDialog.TodoHandler {

    companion object {
        const val KEY_TODO_EDIT = "KEY_TODO_EDIT"

        const val KEY_FIRST = "KEY_FIRST"
    }

    lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title



        initRecyclerView()

        fab.setOnClickListener {
            TodoDialog().show(supportFragmentManager, "TAG_TODO_DIALOG")
        }


        if (isFirstRun()) {
            MaterialTapTargetPrompt.Builder(this@ScrollingActivity)
                .setTarget(findViewById<View>(R.id.fab))
                .setPrimaryText("New Todo Item")
                .setSecondaryText("Tap here to create new todo item")
                .show()
        }

        saveThatItWasStarted()
    }

    private fun isFirstRun(): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
            KEY_FIRST, true
        )
    }

    private fun saveThatItWasStarted() {
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        sp.edit()
            .putBoolean(KEY_FIRST, false)
            .apply()
    }

    private fun initRecyclerView() {
        todoAdapter = TodoAdapter(this)
        recyclerTodo.adapter = todoAdapter

        AppDatabase.getInstance(this).todoDao().getAllTodo()
            .observe(this, Observer { items ->
                todoAdapter.submitList(items)
            })

        val touchCallbakList = TouchCallback(todoAdapter)
        val itemTouchHelper = ItemTouchHelper(touchCallbakList)
        itemTouchHelper.attachToRecyclerView(recyclerTodo)
    }


    override fun todoCreated(todo: Todo) {
        thread {
            AppDatabase.getInstance(this).todoDao().addTodo(todo)

        }
    }

    override fun todoUpdated(todo: Todo) {
        thread {
            AppDatabase.getInstance(this).todoDao().updateTodo(todo)
        }
    }

    public fun showDeleteMessage() {
        Snackbar.make(recyclerTodo, "Todo removed", Snackbar.LENGTH_LONG)
            .setAction("Undo", object: View.OnClickListener {
                override fun onClick(v: View?) {
                    todoAdapter.restoreTodo()
                }
            })
            .show()
    }

    public fun showEditDialog(todoToEdit: Todo) {
        val editDialog = TodoDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_TODO_EDIT, todoToEdit)
        editDialog.arguments = bundle

        editDialog.show(supportFragmentManager, "TAG_ITEM_EDIT")
    }

}