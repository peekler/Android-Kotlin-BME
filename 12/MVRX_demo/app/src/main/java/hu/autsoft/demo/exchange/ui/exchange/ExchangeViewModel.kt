package hu.autsoft.demo.exchange.ui.exchange

import com.airbnb.mvrx.MavericksViewModel

class ExchangeViewModel(state: ExchangeViewState) : MavericksViewModel<ExchangeViewState>(state) {

    fun getRates(baseCurrency: String) {
        setState { copy(isLoading = true) }
        ExchangePresenter.getExchangeData(baseCurrency = baseCurrency,
            onResponse = {
                setState {
                    copy(
                        isLoading = false,
                        exchangeData = it
                    )
                }
            },
            onFailure = {
                setState {
                    copy(
                        isLoading = false,
                        exchangeData = null,
                        exchangedValue = null
                    )
                }
            })
    }

    fun calculate(fromValue: Double?, currency: String) = withState { state ->
        if (state.exchangeData == null || fromValue == null) return@withState

        val rate = state.exchangeData.rates[currency] ?: return@withState

        setState { state.copy(exchangedValue = fromValue * rate) }
    }
}