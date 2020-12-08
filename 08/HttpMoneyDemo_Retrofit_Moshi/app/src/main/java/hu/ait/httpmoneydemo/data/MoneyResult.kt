package hu.ait.httpmoneydemo.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class MoneyResult(val rates: Rates?, val base: String?, val date: String?)

@JsonClass(generateAdapter = true)
class Rates(val CAD: Double?, val HKD: Double?, val ISK: Double?, val PHP: Double?,
            val DKK: Double?, val HUF: Double?, val CZK: Double?, val AUD: Double?, val RON: Double?,
            val SEK: Double?, val IDR: Double?, val INR: Double?, val BRL: Double?, val RUB: Double?,
            val HRK: Double?, val JPY: Double?, val THB: Double?, val CHF: Double?, val SGD: Double?,
            val PLN: Double?, val BGN: Double?, val TRY: Double?, val CNY: Double?, val NOK: Double?,
            val NZD: Double?, val ZAR: Double?, val USD: Double?, val MXN: Double?, val ILS: Double?,
            val GBP: Double?, val KRW: Double?, val MYR: Double?)