package hu.aut.android.kotlinshoppinglist.touch

interface ShoppingTouchHelperAdapter {

    fun onItemDismissed(position: Int)

    fun onItemMoved(fromPosition: Int, toPosition: Int)
}