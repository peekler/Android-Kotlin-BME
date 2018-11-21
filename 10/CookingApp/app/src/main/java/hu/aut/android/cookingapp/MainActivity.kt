package hu.aut.android.cookingapp

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hu.aut.android.recipecontract.RecipeContract
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnInsertToProvider.setOnClickListener {
            insertNewRecipe("Bread")
        }

        btnGetFromProvider.setOnClickListener {
            getRecipes()
        }
    }

    private fun insertNewRecipe(recipe: String): Uri? {
        val values = ContentValues()
        values.put(RecipeContract.RecipeEntry.COLUMN_DATE, Date(System.currentTimeMillis()).toString())
        values.put(RecipeContract.RecipeEntry.COLUMN_TITLE, recipe)
        values.put(RecipeContract.RecipeEntry.COLUMN_DESC, "egg, milk, soda")
        return contentResolver.insert(RecipeContract.RecipeEntry.CONTENT_URI, values)
    }

    private fun getRecipes() {
        val cursor = contentResolver.query(RecipeContract.RecipeEntry.CONTENT_URI,
                null, null, null, null)

        var i = 1
        var data = ""
        while (cursor.moveToNext()) {
            val recipe = cursorToRecipe(cursor)

            data += "${recipe.title} ${recipe.description} nr.  ${i++}\n"
        }

        Toast.makeText(this, data, Toast.LENGTH_LONG).show()
    }

    private fun cursorToRecipe(cursor: Cursor): Recipe {
        return Recipe(
                cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
        )
    }
}