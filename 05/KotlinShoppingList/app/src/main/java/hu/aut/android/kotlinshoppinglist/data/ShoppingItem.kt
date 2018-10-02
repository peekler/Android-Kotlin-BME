package hu.aut.android.kotlinshoppinglist.data

import java.io.Serializable

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "shoppingitem")
data class ShoppingItem(@PrimaryKey(autoGenerate = true) var itemId: Long?,
                        @ColumnInfo(name = "name") var name: String,
                        @ColumnInfo(name = "price") var price: Int,
                        @ColumnInfo(name = "bought") var bought: Boolean
) : Serializable
