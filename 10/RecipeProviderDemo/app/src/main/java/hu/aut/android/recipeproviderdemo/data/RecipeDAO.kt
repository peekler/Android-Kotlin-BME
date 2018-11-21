package hu.aut.android.recipeproviderdemo.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import android.database.Cursor

@Dao
interface RecipeDAO {

    @Query("SELECT * FROM recipes")
    fun findAllRecipes(): Cursor

    @Query("SELECT * FROM recipes WHERE recipeId = :recipeId")
    fun findRecipeById(recipeId: Long): Cursor

    @Insert
    fun insertRecipe(item: Recipe): Long

    @Delete
    fun deleteRecipe(item: Recipe)

    @Update
    fun updateRecipe(item: Recipe)

}
