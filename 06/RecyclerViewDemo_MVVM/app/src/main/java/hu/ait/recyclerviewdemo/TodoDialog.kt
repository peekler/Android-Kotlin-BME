package hu.ait.recyclerviewdemo

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import hu.ait.recyclerviewdemo.data.Todo
import hu.ait.recyclerviewdemo.databinding.TodoDialogBinding
import java.util.*

class TodoDialog : DialogFragment() {

    interface TodoDialogHandler {
        public fun todoCreated(todo: Todo)
    }

    lateinit var todoDialogHandler: TodoDialogHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is TodoDialogHandler) {
            todoDialogHandler = context
        } else {
            throw java.lang.RuntimeException(
                "The activity does not implement the TodoHandler interface")
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle("Todo dialog")

        val dialogViewBinding = TodoDialogBinding.inflate(
            requireActivity().layoutInflater)
        dialogBuilder.setView(dialogViewBinding.root)

        dialogBuilder.setPositiveButton("Ok") {
                dialog, which ->

            todoDialogHandler.todoCreated(
                Todo(
                    null,
                    dialogViewBinding.etTodoText.text.toString(),
                    dialogViewBinding.cbTodoDone.isChecked,
                    Date(System.currentTimeMillis()).toString()
                )
            )
        }
        dialogBuilder.setNegativeButton("Cancel") {
                dialog, which ->
        }


        return dialogBuilder.create()
    }


}