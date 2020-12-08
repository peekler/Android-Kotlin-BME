package hu.bme.aut.rainbowcakedemo.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import kotlinx.coroutines.delay
import java.lang.Exception
import javax.inject.Inject

class MainScreenViewModel @Inject constructor(
    private val mainScreenPresenter: MainScreenPresenter
) : RainbowCakeViewModel<MainScreenViewState>(Initial) {

    class NetworkStatusEvent(val networkType: String) : OneShotEvent

    fun loadRates() = execute {
        viewState = Loading
        //delay(1000)
        viewState = try {
            val moneyResult = mainScreenPresenter.getMoneyResult()
            DataReady(moneyResult.rates?.HUF.toString())
        } catch (e: Exception) {
            NetworkError
        }
    }


    fun checkOnline(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    postEvent(NetworkStatusEvent("NetworkCapabilities.TRANSPORT_CELLULAR"))
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    postEvent(NetworkStatusEvent("NetworkCapabilities.TRANSPORT_WIFI"))
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    postEvent(NetworkStatusEvent("NetworkCapabilities.TRANSPORT_ETHERNET"))
                }
            }
        } else {
            postEvent(NetworkStatusEvent("OFFLINE"))
        }
    }

}