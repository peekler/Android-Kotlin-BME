package hu.ait.recyclerviewdemo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo")
data class Todo(
    @PrimaryKey(autoGenerate = true) var todoId: Long?,
    @ColumnInfo(name = "createData") var createData: String,
    @ColumnInfo(name = "isDone") var isDone: Boolean,
    @ColumnInfo(name = "todoText") var todoText: String
)
