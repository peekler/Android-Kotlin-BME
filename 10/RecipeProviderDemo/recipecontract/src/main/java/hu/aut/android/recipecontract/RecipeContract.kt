package hu.aut.android.recipecontract

import android.net.Uri
import android.provider.BaseColumns

class RecipeContract {

    companion object {
        val CONTENT_AUTHORITY = "hu.aut.android.recipeproviderdemo"
        val BASE_CONTENT_URI = Uri.parse("content://$CONTENT_AUTHORITY")
    }

    class RecipeEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "recipes"

            val COLUMN_DATE = "date"
            val COLUMN_TITLE = "title"
            val COLUMN_DESC = "description"

            //content://hu.aut.android.recipeproviderdemo/recipes
            val CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                    .appendPath(TABLE_NAME)
                    .build()

            fun buildRecipeUriWithId(id: Long): Uri {
                return CONTENT_URI.buildUpon()
                        .appendPath(java.lang.Long.toString(id))
                        .build()
            }
        }
    }
}