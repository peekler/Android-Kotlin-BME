package hu.bme.aut.bottomnavfragmentsdemo.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import hu.bme.aut.bottomnavfragmentsdemo.MainActivity
import hu.bme.aut.bottomnavfragmentsdemo.R
import kotlinx.android.synthetic.main.fragment_one.view.*

class FragmentOne: Fragment() {

    companion object {
        const val TAG="FragmentOne"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView=inflater.inflate(R.layout.fragment_one,container,false)

        rootView.button.setOnClickListener {
            Toast.makeText(activity, "DEMO", Toast.LENGTH_LONG).show()

            (activity as MainActivity).showFragmentByTag(FragmentTwo.TAG, true)
        }

        return rootView
    }
}