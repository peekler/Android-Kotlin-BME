package hu.autsoft.demo.exchange.ui.exchange

import com.airbnb.mvrx.MavericksState
import hu.autsoft.demo.exchange.data.model.ExchangeData

data class ExchangeViewState(
    val isLoading: Boolean = true,
    val exchangeData: ExchangeData? = null,
    val exchangedValue: Double? = null
) : MavericksState