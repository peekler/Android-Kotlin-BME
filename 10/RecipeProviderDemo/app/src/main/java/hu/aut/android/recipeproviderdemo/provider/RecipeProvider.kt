package hu.aut.android.recipeproviderdemo.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import hu.aut.android.recipecontract.RecipeContract
import hu.aut.android.recipeproviderdemo.data.AppDatabase
import hu.aut.android.recipeproviderdemo.data.Recipe

class RecipeProvider : ContentProvider() {

    val CODE_RECIPE = 100
    val CODE_RECIPE_WITH_ID = 101

    private val uriMatcher = buildUriMatcher()

    private fun buildUriMatcher(): UriMatcher {
        val matcher = UriMatcher(UriMatcher.NO_MATCH)
        val authority = RecipeContract.CONTENT_AUTHORITY

        matcher.addURI(authority, RecipeContract.RecipeEntry.TABLE_NAME, CODE_RECIPE)
        matcher.addURI(authority, RecipeContract.RecipeEntry.TABLE_NAME + "/#", CODE_RECIPE_WITH_ID)

        return matcher
    }

    override fun insert(uri: Uri, contentValues: ContentValues): Uri? {
        val insertUri: Uri?

        insertUri = when (uriMatcher.match(uri)) {
            CODE_RECIPE -> {
                var id = AppDatabase.getInstance(context).recipeDAO().insertRecipe(
                        reciperFromContentValues(contentValues)
                )

                if (id != -1L) {
                    context.contentResolver.notifyChange(uri, null)
                }

                RecipeContract.RecipeEntry.buildRecipeUriWithId(id)
            }
            else -> null
        }

        return insertUri
    }

    private fun reciperFromContentValues(recipeContentValues: ContentValues) : Recipe {
        return Recipe(
                null,
                recipeContentValues.getAsString(RecipeContract.RecipeEntry.COLUMN_DATE),
                recipeContentValues.getAsString(RecipeContract.RecipeEntry.COLUMN_TITLE),
                recipeContentValues.getAsString(RecipeContract.RecipeEntry.COLUMN_DESC)
        )
    }


    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {


        val cursor: Cursor = when (uriMatcher.match(uri)) {
            CODE_RECIPE_WITH_ID -> {
                val recipeId = uri.lastPathSegment
                AppDatabase.getInstance(context).recipeDAO().findRecipeById(recipeId.toLong())
            }
            CODE_RECIPE -> {
                AppDatabase.getInstance(context).recipeDAO().findAllRecipes()
            }

            else -> throw UnsupportedOperationException("Unknown uri: $uri")
        }

        cursor.setNotificationUri(context.contentResolver, uri)
        return cursor
    }

    override fun onCreate(): Boolean {
        return true;
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<String>?): Int {
        return 0;
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<String>?): Int {
        return 0;
    }

    override fun getType(p0: Uri): String? {
        return null
    }
}