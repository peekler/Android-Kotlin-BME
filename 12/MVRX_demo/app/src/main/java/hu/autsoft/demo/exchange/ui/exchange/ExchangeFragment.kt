package hu.autsoft.demo.exchange.ui.exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import hu.autsoft.demo.exchange.R
import kotlinx.android.synthetic.main.exchange_fragment.*

class ExchangeFragment : Fragment(), MavericksView {

    private val viewModel: ExchangeViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.exchange_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fromSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.getRates(fromSpinner.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        exchangeButton.setOnClickListener {
            viewModel.calculate(
                fromInput.text.toString().toDoubleOrNull(),
                toSpinner.selectedItem.toString()
            )
        }
    }

    override fun invalidate() = withState(viewModel) { state ->
        when {
            state.isLoading -> {
                inputGroup.isEnabled = false
                progressBar.isVisible = true
            }
            state.exchangeData != null -> {
                inputGroup.isEnabled = true
                progressBar.isVisible = false
                toInput.setText(if (state.exchangedValue == null) null else state.exchangedValue.toString())
            }
            else -> {
                inputGroup.isEnabled = false
                fromSpinner.isEnabled = true
                progressBar.isVisible = false
            }
        }
    }
}