package hu.bme.aut.rainbowcakedemo.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import hu.bme.aut.rainbowcakedemo.R
import kotlinx.android.synthetic.main.fragment_main.*

class MainScreenFragment : RainbowCakeFragment<MainScreenViewState, MainScreenViewModel>() {

    override fun getViewResource() = R.layout.fragment_main

    override fun provideViewModel() = getViewModelFromFactory()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnGet.setOnClickListener {
            viewModel.loadRates()
            //viewModel.checkOnline(requireContext())
        }
    }

    // Képernyő elforgatáskor/konfig változáskor a legutolsó viewState-el lefut újra a render
    override fun render(viewState: MainScreenViewState) {
        when (viewState) {
            Initial -> {
                tvData.text = "INITIAL"
            }
            Loading -> {
                tvData.text = "LOADING"
                tvStatus.visibility = View.VISIBLE
            }
            is DataReady -> {
                tvData.text = viewState.moneyResult
                Toast.makeText(context, viewState.moneyResult, Toast.LENGTH_LONG).show()
            }
            NetworkError -> {
                tvData.text = "NetworkError"
            }
        }.exhaustive
    }

    // A legutolsó esemény nem lövődik újra képernyő elforgatáskor
    override fun onEvent(event: OneShotEvent) {
        when (event) {
            is MainScreenViewModel.NetworkStatusEvent -> {
                tvData.text = event.networkType
            }
        }
    }
}