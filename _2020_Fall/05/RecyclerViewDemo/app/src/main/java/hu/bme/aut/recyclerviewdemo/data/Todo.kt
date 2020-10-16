package hu.bme.aut.recyclerviewdemo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo")
data class Todo(
    @PrimaryKey(autoGenerate = true) var todoId : Long?,
    @ColumnInfo(name = "todotext") val todoText: String,
    @ColumnInfo(name = "createdate") val createDate: String,
    @ColumnInfo(name = "isdone") val isDone: Boolean)