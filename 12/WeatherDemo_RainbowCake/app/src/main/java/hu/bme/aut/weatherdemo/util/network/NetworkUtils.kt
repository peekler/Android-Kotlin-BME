package hu.bme.aut.weatherdemo.util.network

import android.os.Build
import hu.bme.aut.weatherdemo.BuildConfig
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

/**
 * Checks for Internet connection availability with an actual network call.
 * ***Always returns true on emulators!***
 *
 * @return true if the network check succeeds, false otherwise
 */
fun isInternetAvailable(): Boolean {
    if (BuildConfig.DEBUG &&
        (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk" == Build.PRODUCT)
    ) {
        Timber.d("Running on emulator, assuming internet is available")
        return true
    }

    val runtime = Runtime.getRuntime()

    try {
        val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
        val exitValue = ipProcess.waitFor()
        return exitValue == 0
    } catch (se: SecurityException) {
        se.printStackTrace()
    } catch (ioe: IOException) {
        ioe.printStackTrace()
    } catch (ie: InterruptedException) {
        ie.printStackTrace()
    }

    return false
}

/**
 * Executes the given network call and handles the exceptions
 * Wraps the results in a [NetworkResponse]
 */
suspend fun <T : Any> executeNetworkCall(block: suspend () -> T): NetworkResponse<T> {
    /*if (isInternetAvailable().not()) {
        Timber.d("Internet is not available")
        return NetworkUnavailable
    }*/

    return try {
        val networkResult = block.invoke()
        NetworkResult(networkResult)
    } catch (e: IOException) {
        Timber.d("Network fetch failed")
        Timber.d(e)
        NetworkIOError
    } catch (e: HttpException) {
        Timber.d("Network fetch failed")
        Timber.d(e)
        NetworkHttpError(e.code())
    }
}
