package hu.aut.android.kotlinshoppinglist.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShoppingItemDAO {

    @Query("SELECT * FROM shoppingitem")
    fun findAllItems(): LiveData<List<ShoppingItem>>

    @Insert
    fun insertItem(item: ShoppingItem): Long

    @Delete
    fun deleteItem(item: ShoppingItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateItem(item: ShoppingItem)

}
