package hu.aut.fragmentdemo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment: Fragment() {

    companion object {
        const val TAG="HomeFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView=inflater.inflate(R.layout.fragment_home,container,false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailsButton.setOnClickListener{
            val mainActivity=activity as MainActivity
            mainActivity.showDetails(nameEditText.text.toString())
        }
    }

}