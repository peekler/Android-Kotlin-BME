package hu.ait.recyclerviewdemo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import hu.ait.recyclerviewdemo.data.AppDatabase
import hu.ait.recyclerviewdemo.data.Todo
import hu.ait.recyclerviewdemo.data.TodoDAO
import hu.ait.recyclerviewdemo.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodosViewModel(application: Application) : AndroidViewModel(application) {

    val allTodos: LiveData<List<Todo>>

    private val repository : TodoRepository

    private var todoDAO: TodoDAO

    var score = 0

    init {
        todoDAO = AppDatabase.getInstance(application).todoDao()
        repository = TodoRepository(todoDAO)
        //allTodos = todoDAO.getAllTodos()
        allTodos = repository.getAllTodos()
    }

    fun insertTodo(todo: Todo) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertTodo(todo)
    }

    fun insertTodo2(todo: Todo)  {
        /*Thread {
            todoDAO.insertTodo(todo)
        }.start()*/
        repository.insertTodo2(todo)
    }

    fun deleteTodo(todo: Todo) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTodo(todo)
    }

}