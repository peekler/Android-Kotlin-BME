package hu.ait.todorecyclerview.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.ait.todorecyclerview.R
import hu.ait.todorecyclerview.ScrollingActivity
import hu.ait.todorecyclerview.data.AppDatabase
import hu.ait.todorecyclerview.data.Todo
import hu.ait.todorecyclerview.touch.TouchHelper
import kotlinx.android.synthetic.main.todo_row.view.*
import java.util.*
import kotlin.concurrent.thread

class TodoAdapter(var context: Context) : ListAdapter<Todo, TodoAdapter.ViewHolder>(TodoDiffCallback()), TouchHelper {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.todo_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = getItem(position)

        holder.cbDone.text = todo.todoText
        holder.cbDone.isChecked = todo.done
        holder.tvDate.text = todo.createDate

        holder.btnDelete.setOnClickListener {
            deleteTodo(holder.adapterPosition)
        }

        holder.btnEdit.setOnClickListener {
            (context as ScrollingActivity).showEditDialog(todo)
        }

        holder.cbDone.setOnClickListener {
            todo.done = holder.cbDone.isChecked
            thread {
                AppDatabase.getInstance(context).todoDao().updateTodo(todo)
            }
        }
    }

    private var deletedTodo: Todo? = null
    private var deleteIndex = -1

    fun deleteTodo(position: Int) {
        deletedTodo = getItem(position)
        deleteIndex = position

        thread {
            AppDatabase.getInstance(context).todoDao().deleteTodo(deletedTodo!!)
        }

        (context as ScrollingActivity).showDeleteMessage()
    }

    fun restoreTodo() {
        thread {
            AppDatabase.getInstance(context).todoDao().addTodo(deletedTodo!!)
        }
    }

    override fun onDismissed(position: Int) {
        deleteTodo(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        notifyItemMoved(fromPosition, toPosition)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate = itemView.tvDate
        val cbDone = itemView.cbDone
        val btnDelete = itemView.btnDelete
        val btnEdit = itemView.btnEdit
    }

}


class TodoDiffCallback : DiffUtil.ItemCallback<Todo>() {
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.todoId == newItem.todoId
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem == newItem
    }
}
