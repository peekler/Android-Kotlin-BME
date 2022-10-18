package hu.bme.aut.shoppinglist

import android.os.Bundle
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Adapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import hu.bme.aut.shoppinglist.adapter.ShoppingAdapter
import hu.bme.aut.shoppinglist.data.AppDatabase
import hu.bme.aut.shoppinglist.data.ShoppingItem
import kotlinx.android.synthetic.main.activity_scrolling.*

class ScrollingActivity :
    AppCompatActivity(), ShoppingItemDialog.ShoppingItemDialogHandler {

    companion object {
        const val KEY_EDIT = "KEY_EDIT"
    }

    private lateinit var adapter: ShoppingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(findViewById(R.id.toolbar))

        initRecyclerView()

        fab.setOnClickListener {
            ShoppingItemDialog().show(supportFragmentManager, "TAG_SHOP_DIALOG")
        }

    }


    private fun initRecyclerView() {
       Thread {
            var shoppingItemList =
                AppDatabase.getInstance(this@ScrollingActivity).
                shoppingDao().getAllShoppingItems()

            runOnUiThread {
                adapter = ShoppingAdapter(this, shoppingItemList)
                recyclerShopping.adapter = adapter
            }
        }.start()

    }


    var editIndex = -1

    fun showEditDialog(shoppingItem: ShoppingItem, adapterPosition: Int) {
        editIndex = adapterPosition
        // show edit dialog

        val editDialog = ShoppingItemDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_EDIT, shoppingItem)
        editDialog.arguments = bundle

        editDialog.show(supportFragmentManager, "EDITDIALOG")
    }


    override fun shoppingItemCreated(item: ShoppingItem) {
        Thread {
            var newId = AppDatabase.getInstance(this@ScrollingActivity).shoppingDao()
                .insertShoppingItem(item)
            item.itemId = newId
            runOnUiThread {
                adapter.addItem(item)
            }
        }.start()
    }

    override fun shoppingItemUpdated(item: ShoppingItem) {
        Thread {
            AppDatabase.getInstance(this@ScrollingActivity).shoppingDao()
                .updateShoppingItem(item)
            runOnUiThread {
                adapter.updateItem(item, editIndex)
            }
        }.start()
    }


}