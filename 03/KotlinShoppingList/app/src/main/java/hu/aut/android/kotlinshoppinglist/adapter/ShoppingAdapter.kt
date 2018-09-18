package hu.aut.android.kotlinshoppinglist.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import hu.aut.android.kotlinshoppinglist.MainActivity
import hu.aut.android.kotlinshoppinglist.R
import hu.aut.android.kotlinshoppinglist.adapter.ShoppingAdapter.ViewHolder
import hu.aut.android.kotlinshoppinglist.data.AppDatabase
import hu.aut.android.kotlinshoppinglist.data.ShoppingItem
import hu.aut.android.kotlinshoppinglist.touch.ShoppingTouchHelperAdapter
import kotlinx.android.synthetic.main.row_item.view.*
import java.util.*

class ShoppingAdapter : RecyclerView.Adapter<ViewHolder>, ShoppingTouchHelperAdapter {

    private val items = mutableListOf<ShoppingItem>()
    private val context: Context

    constructor(context: Context, items: List<ShoppingItem>) : super() {
        this.context = context
        this.items.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.row_item, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = items[position].name
        holder.tvPrice.text = items[position].price.toString()
        holder.cbBought.isChecked = items[position].bought

        holder.btnDelete.setOnClickListener {
            deleteItem(holder.adapterPosition)
        }

        holder.btnEdit.setOnClickListener {
            (holder.itemView.context as MainActivity).showEditItemDialog(
                    items[holder.adapterPosition])
        }

        holder.cbBought.setOnClickListener {
            items[position].bought = holder.cbBought.isChecked
            val dbThread = Thread {
                AppDatabase.getInstance(context).shoppingItemDao().updateItem(items[position])
            }
            dbThread.start()
        }
    }

    fun addItem(item: ShoppingItem) {
        items.add(item)
        notifyItemInserted(items.lastIndex)
    }

    fun deleteItem(position: Int) {
        val dbThread = Thread {
            AppDatabase.getInstance(context).shoppingItemDao().deleteItem(
                    items[position])
            (context as MainActivity).runOnUiThread{
                items.removeAt(position)
                notifyItemRemoved(position)
            }
        }
        dbThread.start()
    }

    fun updateItem(item: ShoppingItem) {
        val idx = items.indexOf(item)
        items[idx] = item
        notifyItemChanged(idx)
    }

    override fun onItemDismissed(position: Int) {
        deleteItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(items, fromPosition, toPosition)

        notifyItemMoved(fromPosition, toPosition)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.tvName
        val tvPrice: TextView = itemView.tvPrice
        val cbBought: CheckBox = itemView.cbBought
        val btnDelete: Button = itemView.btnDelete
        val btnEdit: Button = itemView.btnEdit
    }
}