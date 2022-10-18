package hu.ait.recyclerviewdemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import hu.ait.recyclerviewdemo.data.Todo
import hu.ait.recyclerviewdemo.databinding.ActivityMainBinding
import hu.ait.recyclerviewdemo.viewmodel.TodosViewModel


class MainActivity : AppCompatActivity(), TodoDialog.TodoDialogHandler {
    private lateinit var todosViewModel: TodosViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TodoRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todosViewModel = ViewModelProvider(this)[TodosViewModel::class.java]

        binding.btnAdd.setOnClickListener {
            //TodoDialog().show(supportFragmentManager,"TAG_TODO")
            todosViewModel.score++
        }

        binding.btnGet.setOnClickListener {
            //TodoDialog().show(supportFragmentManager,"TAG_TODO")
            Toast.makeText(this, "${todosViewModel.score}", Toast.LENGTH_SHORT).show()
        }

        initRecyclerView()

    }

    private fun initRecyclerView() {
        adapter = TodoRecyclerAdapter(this, todosViewModel)
        binding.recyclerTodo.adapter = adapter

        todosViewModel.allTodos.observe(this) { cities ->
            adapter.submitList(cities)
        }
    }

    override fun todoCreated(todo: Todo) {
        todosViewModel.insertTodo2(todo)
    }
}