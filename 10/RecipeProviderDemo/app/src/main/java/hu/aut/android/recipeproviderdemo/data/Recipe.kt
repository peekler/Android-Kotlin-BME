package hu.aut.android.recipeproviderdemo.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(@PrimaryKey(autoGenerate = true) var recipeId: Long?,
                  @ColumnInfo(name = "date") var date: String,
                  @ColumnInfo(name = "title") var title: String,
                  @ColumnInfo(name = "description") var description: String
)
