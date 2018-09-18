package hu.aut.android.kotlinshoppinglist.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

@Dao
interface ShoppingItemDAO {

    @Query("SELECT * FROM shoppingitem")
    fun findAllItems(): List<ShoppingItem>

    @Insert
    fun insertItem(item: ShoppingItem): Long

    @Delete
    fun deleteItem(item: ShoppingItem)

    @Update
    fun updateItem(item: ShoppingItem)

}
