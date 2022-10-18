package hu.ait.todorecyclerview.touch

interface TouchHelper {
    fun onDismissed(position: Int)
    fun onItemMoved(fromPosition: Int, toPosition: Int)
}