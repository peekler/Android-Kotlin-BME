package hu.ait.todorecyclerview

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.ait.todorecyclerview.data.Todo
import kotlinx.android.synthetic.main.todo_dialog.view.*
import java.lang.RuntimeException
import java.util.*

class TodoDialog : DialogFragment() {

    interface TodoHandler {
        fun todoCreated(todo: Todo)
        fun todoUpdated(todo: Todo)
    }

    lateinit var todoHandler: TodoHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is TodoHandler) {
            todoHandler = context
        } else {
            throw RuntimeException(
                "The Activity is not implementing the TodoHandler interface."
            )
        }
    }

    lateinit var etTodoText : EditText
    lateinit var cbTodoDone : CheckBox

    var inEditMode = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle("Todo dialog")

        val dialogView = requireActivity().layoutInflater.inflate(
            R.layout.todo_dialog, null
        )
        etTodoText = dialogView.etTodoText
        cbTodoDone = dialogView.cbTodoDone
        dialogBuilder.setView(dialogView)

        inEditMode = (this.arguments != null && this.arguments!!.containsKey(ScrollingActivity.KEY_TODO_EDIT))

        if (inEditMode) {
            val todoEdit = this.arguments!!.getSerializable(ScrollingActivity.KEY_TODO_EDIT) as Todo
            etTodoText.setText(todoEdit.todoText)
            cbTodoDone.isChecked = todoEdit.done
        }

        dialogBuilder.setPositiveButton("Ok") {
            dialog, which ->
            //
        }
        dialogBuilder.setNegativeButton("Cancel") {
            dialog, which ->
            //
        }

        return dialogBuilder.create()
    }

    override fun onResume() {
        super.onResume()

        val dialog = dialog as AlertDialog
        val positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)

        positiveButton.setOnClickListener {
            if (etTodoText.text.isNotEmpty()) {
                if (inEditMode) {
                    handleTodoEdit()
                } else {
                    handleTodoCreate()
                }

                dialog.dismiss()
            } else {
                etTodoText.error = "This field can not be empty"
            }
        }
    }

    private fun handleTodoCreate() {
        todoHandler.todoCreated(Todo(
            null,
            Date(System.currentTimeMillis()).toString(),
            false,
            etTodoText.text.toString(),
        ))
    }

    private fun handleTodoEdit() {
        val todoToEdit = (arguments?.getSerializable(
            ScrollingActivity.KEY_TODO_EDIT) as Todo).copy(
            todoText =  etTodoText.text.toString(),
            done = cbTodoDone.isChecked
        )

        todoHandler.todoUpdated(todoToEdit)
    }
}