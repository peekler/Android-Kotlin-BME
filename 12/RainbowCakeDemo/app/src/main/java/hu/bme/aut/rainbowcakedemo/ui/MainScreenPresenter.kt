package hu.bme.aut.rainbowcakedemo.ui

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.rainbowcakedemo.domain.MoneyRatesInteractor
import hu.bme.aut.rainbowcakedemo.network.model.MoneyResult
import javax.inject.Inject

class MainScreenPresenter @Inject constructor(
    private val moneyRatesInteractor: MoneyRatesInteractor
) {
    suspend fun getMoneyResult(): MoneyResult {
        return moneyRatesInteractor.getMoneyRates()
    }
}
