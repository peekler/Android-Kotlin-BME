package hu.ait.recyclerviewdemo.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface TodoDAO {
    @Query("SELECT * FROM todo")
    fun getAllTodos(): LiveData<List<Todo>>

    @Insert
    fun insertTodo(vararg todo: Todo)

    @Delete
    fun deleteTodo(grade: Todo)

    @Query("DELETE FROM todo")
    fun deleteAllTodos()
}
