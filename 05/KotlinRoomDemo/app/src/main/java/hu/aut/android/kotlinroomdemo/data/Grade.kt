package hu.aut.android.kotlinroomdemo.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "grade")
data class Grade(@PrimaryKey(autoGenerate = true) var gradeId: Long?,
                       @ColumnInfo(name = "studentid") var studentId: String,
                       @ColumnInfo(name = "grade") var grade: String

)