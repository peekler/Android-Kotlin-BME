package hu.ait.recyclerviewdemo

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.ait.recyclerviewdemo.data.Todo
import hu.ait.recyclerviewdemo.databinding.TodoRowBinding
import hu.ait.recyclerviewdemo.viewmodel.TodosViewModel

class TodoRecyclerAdapter(
    private val context: Context,
    private val todosViewModel: TodosViewModel
) : ListAdapter<Todo, TodoRecyclerAdapter.ViewHolder>(TodoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val todoBinding = TodoRowBinding.inflate(
            LayoutInflater.from(context),
            parent, false
        )
        return ViewHolder(todoBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = getItem(holder.adapterPosition)
        holder.bind(todo)
    }

    inner class ViewHolder(var binding: TodoRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Todo) {
            binding.tvDate.text = todo.createData
            binding.cbDone.text = todo.todoText
            binding.cbDone.isChecked = todo.isDone
        }
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