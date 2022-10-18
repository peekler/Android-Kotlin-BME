package hu.ait.recyclerviewdemo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import hu.ait.recyclerviewdemo.data.Todo
import hu.ait.recyclerviewdemo.data.TodoDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TodoRepository(private val todoDao: TodoDAO) {

    fun getAllTodos() : LiveData<List<Todo>> {
        return todoDao.getAllTodos()
    }

    suspend fun insertTodo(todo: Todo) {
        todoDao.insertTodo(todo)
    }

    fun insertTodo2(todo: Todo)  {
        Thread {
            todoDao.insertTodo(todo)
        }.start()
    }

    suspend fun deleteTodo(todo: Todo)  {
        todoDao.deleteTodo(todo)
    }
}