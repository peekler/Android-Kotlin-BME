package hu.aut.android.kotlinshoppinglist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import hu.aut.android.kotlinshoppinglist.adapter.ShoppingAdapter
import kotlinx.android.synthetic.main.activity_main.*
import android.R.id.edit
import android.app.SearchManager
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import hu.aut.android.kotlinshoppinglist.data.AppDatabase
import hu.aut.android.kotlinshoppinglist.data.ShoppingItem
import hu.aut.android.kotlinshoppinglist.touch.ShoppingTouchHelperCallback
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(), ShoppingItemDialog.ShoppingItemHandler {
    companion object {
        val KEY_FIRST = "KEY_FIRST"
        val KEY_ITEM_TO_EDIT = "KEY_ITEM_TO_EDIT"
    }

    private lateinit var adapter: ShoppingAdapter
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            ShoppingItemDialog().show(supportFragmentManager, "TAG_ITEM")
        }

        initRecyclerView()

        if (isFirstRun()) {
            MaterialTapTargetPrompt.Builder(this@MainActivity)
                .setTarget(findViewById<View>(R.id.fab))
                .setPrimaryText("New Shopping Item")
                .setSecondaryText("Tap here to create new shopping item")
                .show()
        }

        saveThatItWasStarted()
    }

    private fun isFirstRun(): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
            KEY_FIRST, true
        )
    }

    private fun saveThatItWasStarted() {
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        sp.edit()
            .putBoolean(KEY_FIRST, false)
            .apply()
    }

    private fun initRecyclerView() {
        adapter = ShoppingAdapter(this)
        recyclerShopping.adapter = adapter

        AppDatabase.getInstance(this).shoppingItemDao().findAllItems()
            .observe(this, Observer { items ->
                adapter.submitList(items)
            })

        val callback = ShoppingTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerShopping)

        swiperefresh.setOnRefreshListener {
            // This method performs the actual data-refresh operation.
            // The method calls setRefreshing(false) when it's finished.
            AppDatabase.getInstance(this).shoppingItemDao().findAllItems()
                .observe(this, Observer { items ->
                    adapter.submitList(items)
                })

            searchView?.setQuery("",false)
            swiperefresh.isRefreshing = false
        }
    }

    fun showEditItemDialog(itemToEdit: ShoppingItem) {
        val editDialog = ShoppingItemDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_TO_EDIT, itemToEdit)
        editDialog.arguments = bundle

        editDialog.show(supportFragmentManager, "TAG_ITEM_EDIT")
    }


    override fun shoppingItemCreated(item: ShoppingItem) {
        thread {
            AppDatabase.getInstance(this@MainActivity).shoppingItemDao()
                .insertItem(item)
        }
    }

    override fun shoppingItemUpdated(item: ShoppingItem) {
        thread {
            AppDatabase.getInstance(this@MainActivity).shoppingItemDao()
                .updateItem(item)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                AppDatabase.getInstance(this@MainActivity).shoppingItemDao().findItems(query!!)
                    .observe(this@MainActivity, Observer { items ->
                        adapter.submitList(items)
                    })

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isEmpty()) {
                    AppDatabase.getInstance(this@MainActivity).shoppingItemDao().findAllItems()
                        .observe(this@MainActivity, Observer { items ->
                            adapter.submitList(items)
                        })
                }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}
