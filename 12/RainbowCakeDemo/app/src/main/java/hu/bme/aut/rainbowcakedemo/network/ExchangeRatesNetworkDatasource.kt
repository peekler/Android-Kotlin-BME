package hu.bme.aut.rainbowcakedemo.network

import hu.bme.aut.rainbowcakedemo.network.model.MoneyResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRatesNetworkDataSource @Inject constructor(
    private val exchnageRatesAPI: ExchnageRatesAPI
) {

    suspend fun getMoneyRates(): MoneyResult {
        var result = exchnageRatesAPI.getMoney("USD")
        return result
    }

}
