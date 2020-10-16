package hu.aut.android.kotlinshoppinglist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "shoppingitem")
data class ShoppingItem(@PrimaryKey(autoGenerate = true) var itemId: Long?,
                        @ColumnInfo(name = "name") var name: String,
                        @ColumnInfo(name = "price") var price: Int,
                        @ColumnInfo(name = "bought") var bought: Boolean
) : Serializable
