package hu.aut.android.kotlindialogfragmentdemo.fragment

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.widget.EditText
import android.view.LayoutInflater
import hu.aut.android.kotlindialogfragmentdemo.MainActivity
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import hu.aut.android.kotlindialogfragmentdemo.R
import kotlinx.android.synthetic.main.layout_dialog.view.*


class MessageFragment : DialogFragment() {

    private var onMessageFragmentAnswer: OnMessageFragmentAnswer? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnMessageFragmentAnswer) {
            onMessageFragmentAnswer = context
        } else {
            throw RuntimeException(
                    "This Activity is not implementing the " +
                            "OnMessageFragmentAnswer interface")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = arguments?.getString(MainActivity.KEY_MSG)

        val alertDialogBuilder = AlertDialog.Builder(requireContext())

        val inflater = LayoutInflater.from(getContext())
        val dialogLayout = inflater.inflate(R.layout.layout_dialog, null)
        val etName = dialogLayout.etName
        alertDialogBuilder.setView(dialogLayout)


        alertDialogBuilder.setTitle("Please read this message")
        alertDialogBuilder.setMessage(message)

        alertDialogBuilder.setPositiveButton("Okay", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
            onMessageFragmentAnswer!!.onPositiveSelected(etName.text.toString())
        })
        alertDialogBuilder.setNegativeButton("Nope", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
            onMessageFragmentAnswer!!.onNegativeSelected()
        })


        return alertDialogBuilder.create()
    }
}
