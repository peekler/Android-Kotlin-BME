package hu.aut.android.kotlindialogfragmentdemo.fragment

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog


class SelectFruitFragment : DialogFragment(), DialogInterface.OnClickListener {

    private val options = arrayOf("Apple", "Orange", "Lemon")
    private var optionsFragmentInterface: OptionsFragmentInterface? = null

    interface OptionsFragmentInterface {
        fun onOptionsFragmentResult(fruit: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            optionsFragmentInterface = context as OptionsFragmentInterface
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement OptionsFragmentInterface")
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Please select")
        builder.setItems(options, this)

        return builder.create()
    }

    override fun onClick(dialog: DialogInterface,
                         choice: Int) {
        optionsFragmentInterface?.onOptionsFragmentResult(
                options[choice])
    }

    companion object {
        val TAG = "OptionsFragment"
    }
}
