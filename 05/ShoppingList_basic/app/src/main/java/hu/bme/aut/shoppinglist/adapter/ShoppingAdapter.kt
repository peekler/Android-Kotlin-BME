package hu.bme.aut.shoppinglist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.shoppinglist.R
import hu.bme.aut.shoppinglist.ScrollingActivity
import hu.bme.aut.shoppinglist.data.AppDatabase
import hu.bme.aut.shoppinglist.data.ShoppingItem
import kotlinx.android.synthetic.main.shop_item.view.*

class ShoppingAdapter : RecyclerView.Adapter<ShoppingAdapter.ViewHolder> {

    private val items = mutableListOf<ShoppingItem>()
    private val context: Context

    constructor(context: Context, itemsList: List<ShoppingItem>) : super() {
        this.context = context
        items.addAll(itemsList)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.shop_item, parent, false
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

        if (items[position].category == 0) {
            holder.ivItemLogo.setImageResource(R.drawable.food)
        }
        else if (items[position].category == 1) {
            holder.ivItemLogo.setImageResource(R.drawable.clothes)
        }
        else if (items[position].category == 2) {
            holder.ivItemLogo.setImageResource(R.drawable.sport)
        }

        holder.btnDelete.setOnClickListener {
            deleteItem(holder.adapterPosition)
        }

        holder.btnEdit.setOnClickListener {
            (context as ScrollingActivity).showEditDialog(
                items[holder.adapterPosition], holder.adapterPosition
            )
        }
    }

    fun deleteItem(position: Int) {
        try {
            var itemToDelete = items.get(position)
            Thread {
                AppDatabase.getInstance(context).shoppingDao().
                    deleteShoppingItem(itemToDelete)

                (context as ScrollingActivity).runOnUiThread {
                    items.removeAt(position)
                    notifyItemRemoved(position)
                }
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addItem(item: ShoppingItem) {
        items.add(item)
        //notifyDataSetChanged()
        notifyItemInserted(items.lastIndex)
    }

    fun updateItem(item: ShoppingItem, editIndex: Int) {
        items.set(editIndex, item)
        notifyItemChanged(editIndex)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.tvName
        val tvPrice: TextView = itemView.tvPrice
        val cbBought: CheckBox = itemView.cbBought
        val ivItemLogo: ImageView = itemView.ivItemLogo
        val btnDelete: Button = itemView.btnDelete
        val btnEdit: Button = itemView.btnEdit
    }
}