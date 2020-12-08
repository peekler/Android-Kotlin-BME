package hu.autsoft.demo.exchange.ui.exchange

import hu.autsoft.demo.exchange.data.interactor.ExchangeInteractor
import hu.autsoft.demo.exchange.data.model.ExchangeData

object ExchangePresenter {

    fun getExchangeData(
        baseCurrency: String,
        onResponse: (ExchangeData) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        ExchangeInteractor.getExchangeData(baseCurrency, onResponse, onFailure)
    }
}