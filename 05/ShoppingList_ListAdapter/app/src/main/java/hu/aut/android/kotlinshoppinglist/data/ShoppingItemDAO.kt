package hu.aut.android.kotlinshoppinglist.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShoppingItemDAO {

    @Query("SELECT * FROM shoppingitem")
    fun findAllItems(): LiveData<List<ShoppingItem>>

    @Query("SELECT * FROM shoppingitem WHERE name LIKE '%' || :search || '%'")
    fun findItems(search: String): LiveData<List<ShoppingItem>>

    @Insert
    fun insertItem(item: ShoppingItem): Long

    @Delete
    fun deleteItem(item: ShoppingItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateItem(item: ShoppingItem)

}
