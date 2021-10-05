package hu.bme.aut.bottomnavfragmentsdemo.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hu.bme.aut.bottomnavfragmentsdemo.R

class FragmentTwo: Fragment() {

    companion object {
        const val TAG="FragmentTwo"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView=inflater.inflate(R.layout.fragment_two,container,false)

        return rootView
    }
}