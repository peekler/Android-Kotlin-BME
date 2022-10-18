package hu.aut.android.kotlinshoppinglist.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import hu.aut.android.kotlinshoppinglist.MainActivity
import hu.aut.android.kotlinshoppinglist.R
import hu.aut.android.kotlinshoppinglist.adapter.ShoppingAdapter.ViewHolder
import hu.aut.android.kotlinshoppinglist.data.AppDatabase
import hu.aut.android.kotlinshoppinglist.data.ShoppingItem
import hu.aut.android.kotlinshoppinglist.databinding.RowItemBinding
import hu.aut.android.kotlinshoppinglist.touch.ShoppingTouchHelperAdapter
import kotlinx.android.synthetic.main.row_item.view.*
import java.util.*
import kotlin.concurrent.thread

class ShoppingAdapter(var context: Context) :
    ListAdapter<ShoppingItem, ViewHolder>(ShopDiffCallback()), ShoppingTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

        with(holder) {
            binding.btnDelete.setOnClickListener {
                thread {
                    AppDatabase.getInstance(context).shoppingItemDao().deleteItem(item)
                }
            }

            binding.btnEdit.setOnClickListener {
                (holder.itemView.context as MainActivity).showEditItemDialog(
                    item
                )
            }

            binding.cbBought.setOnClickListener {
                item.bought = binding.cbBought.isChecked
                thread {
                    AppDatabase.getInstance(context).shoppingItemDao().updateItem(item)
                }
            }
        }
    }

    override fun onItemDismissed(position: Int) {
        thread {
            AppDatabase.getInstance(context).shoppingItemDao().deleteItem(getItem(position))
        }
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        notifyItemMoved(fromPosition, toPosition)
    }

    inner class ViewHolder(val binding: RowItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShoppingItem) {
            binding.item = item
        }
    }

}

class ShopDiffCallback : DiffUtil.ItemCallback<ShoppingItem>() {
    override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
        return oldItem.itemId == newItem.itemId
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
        return oldItem == newItem
    }
}