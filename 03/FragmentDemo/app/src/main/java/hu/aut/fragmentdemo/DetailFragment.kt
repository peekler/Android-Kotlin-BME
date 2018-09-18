package hu.aut.fragmentdemo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment(): Fragment() {


    companion object {
        const val TAG="DetailFragment"

        private const val NAME="NAME"

        fun newInstance(name:String):DetailFragment{
            val fragment=DetailFragment()
            val bundle=Bundle()
            bundle.putString(NAME,name)

            fragment.arguments=bundle
            return  fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView=inflater.inflate(R.layout.fragment_detail,container,false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name=arguments!!.getString(NAME)

        nameTextView.text="Hello $name"
    }

}