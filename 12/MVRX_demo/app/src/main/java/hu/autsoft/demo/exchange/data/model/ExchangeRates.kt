package hu.autsoft.demo.exchange.data.model

data class ExchangeData(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)