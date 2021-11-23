package hu.bme.aut.navcontrollertestdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {

    companion object {
        const val KEY_PERSON = "KEY_PERSON"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.btnGo.setOnClickListener {
            /*val bundle = Bundle()
            bundle.putSerializable(KEY_PERSON, Person(etName.text.toString(), etAddress.text.toString()))
            view.findNavController().navigate(
                R.id.action_mainScreenFragment_to_detailFragment,
                bundle)*/

            // new solution with SafeArgs
            view.findNavController().navigate(
                MainFragmentDirections.actionMainScreenFragmentToDetailFragment(
                Person(etName.text.toString(), etAddress.text.toString())))
        }
    }

}