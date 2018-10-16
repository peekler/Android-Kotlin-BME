package hu.aut.android.asynctaskhttpdemo.retrofit


data class MoneyResult(
    var date: String,
    var rates: Rates,
    var base: String
)

data class Rates(
    var BGN: Double,
    var CAD: Double,
    var BRL: Double,
    var HUF: Double,
    var DKK: Double,
    var JPY: Double,
    var ILS: Double,
    var TRY: Double,
    var RON: Double,
    var GBP: Double,
    var PHP: Double,
    var HRK: Double,
    var NOK: Double,
    var USD: Double,
    var MXN: Double,
    var AUD: Double,
    var IDR: Double,
    var KRW: Double,
    var HKD: Double,
    var ZAR: Double,
    var ISK: Double,
    var CZK: Double,
    var THB: Double,
    var MYR: Double,
    var NZD: Double,
    var PLN: Double,
    var SEK: Double,
    var RUB: Double,
    var CNY: Double,
    var SGD: Double,
    var CHF: Double,
    var INR: Double
)