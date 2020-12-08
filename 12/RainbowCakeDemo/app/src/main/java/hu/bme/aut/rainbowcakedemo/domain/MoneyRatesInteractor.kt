package hu.bme.aut.rainbowcakedemo.domain

import hu.bme.aut.rainbowcakedemo.network.ExchangeRatesNetworkDataSource
import hu.bme.aut.rainbowcakedemo.network.model.MoneyResult
import javax.inject.Inject

class MoneyRatesInteractor @Inject constructor(
    private val networkDataSource: ExchangeRatesNetworkDataSource
) {

    suspend fun getMoneyRates(): MoneyResult {
        return networkDataSource.getMoneyRates()
    }

}