package hu.bme.aut.shoppinglist.data

import androidx.room.*

@Dao
interface ShoppingDao {

    @Query("SELECT * FROM shoppingitems")
    fun getAllShoppingItems(): List<ShoppingItem>

    @Insert
    fun insertShoppingItem(todo: ShoppingItem) : Long

    @Delete
    fun deleteShoppingItem(todo: ShoppingItem)

    @Update
    fun updateShoppingItem(todo: ShoppingItem)

}
