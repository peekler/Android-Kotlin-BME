package hu.aut.android.kotlindialogfragmentdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hu.aut.android.kotlindialogfragmentdemo.fragment.OnMessageFragmentAnswer
import hu.aut.android.kotlindialogfragmentdemo.fragment.SelectFruitFragment
import kotlinx.android.synthetic.main.activity_main.*
import hu.aut.android.kotlindialogfragmentdemo.fragment.MessageFragment



class MainActivity : AppCompatActivity(),
        SelectFruitFragment.OptionsFragmentInterface, OnMessageFragmentAnswer   {

    override fun onPositiveSelected(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    override fun onNegativeSelected() {
        Toast.makeText(this, "NOPE was selected", Toast.LENGTH_SHORT).show()
    }

    companion object {
        val KEY_MSG = "KEY_MSG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnDialog1.setOnClickListener{
            val messageFragment = MessageFragment()
            messageFragment.isCancelable = false

            val bundle = Bundle()
            bundle.putString(KEY_MSG, "HELLLOO")
            messageFragment.arguments = bundle

            messageFragment.show(supportFragmentManager,
                    "MessageFragment")
        }

        btnDialog2.setOnClickListener{
            SelectFruitFragment().show(
                    supportFragmentManager, SelectFruitFragment.TAG)
        }

    }

    override fun onOptionsFragmentResult(fruit: String) {
        Toast.makeText(this, "Selected fruit: "+fruit, Toast.LENGTH_LONG).show();
    }
}
