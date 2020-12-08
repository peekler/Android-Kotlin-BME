package hu.bme.aut.weatherdemo.util.network

/**
 *                                               DataTransferSuccess<T>
 *                                             /
 *                             SomeResult<T> - NetworkUnavailableCached<T>
 *                           /                 \
 *                         /                    NetworkErrorCached<T>
 *  DataTransferResponse<T>
 *                         \               NetworkUnavailableNotCached
 *                          \            /
 *                           NoResult - NetworkErrorNotCached
 */
@Suppress("unused")
sealed class DataTransferResponse<out T : Any>


/**
 * Base class for results where some data was fetched from a data source.
 */
sealed class SomeResult<out T : Any>(val result: T) : DataTransferResponse<T>()

/**
 * The network call succeeded, the result was stored in the local database.
 */
class DataTransferSuccess<out T : Any>(result: T) : SomeResult<T>(result)

/**
 * Network was unavailable, but the required data was retrieved from the local database.
 */
class NetworkUnavailableCached<out T : Any>(result: T) : SomeResult<T>(result)

/**
 * Network is available, but the network call failed and the required data was instead
 * retrieved from the local database.
 */
class NetworkErrorCached<out T : Any>(result: T) : SomeResult<T>(result)


/**
 * Base class for results where no data could be fetched from the data sources.
 */
sealed class NoResult : DataTransferResponse<Nothing>()

/**
 * Network is unavailable and the required data is not in the local database either.
 */
object NetworkUnavailableNotCached : NoResult()

/**
 * Network is available, but the network call has failed and the required data is not
 * in the local database either.
 */
object NetworkErrorNotCached : NoResult()
