package hu.bme.aut.recyclerviewdemo.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDAO {
    @Query("SELECT * FROM todo")
    fun getAllTodo(): LiveData<List<Todo>>

    @Insert
    fun insertTodo(todo: Todo) : Long

    @Delete
    fun deleteTodo(todo: Todo)

}