package hu.bme.aut.recyclerviewdemo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.recyclerviewdemo.R
import hu.bme.aut.recyclerviewdemo.data.Todo
import kotlinx.android.synthetic.main.todo_row.view.*

class TodoAdapter(val context: Context) : ListAdapter<Todo, TodoAdapter.ViewHolder>(TodoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = getItem(position)

        holder.cbTodo.text = todo.todoText
        holder.cbTodo.isChecked = todo.isDone
        holder.tvDate.text = todo.createDate
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cbTodo = itemView.cbTodo
        val tvDate = itemView.tvDate
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