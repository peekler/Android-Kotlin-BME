package hu.aut.android.asynctaskhttpdemo

import android.content.Context
import android.support.v4.content.LocalBroadcastManager
import android.content.Intent
import android.os.AsyncTask
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class HttpGetTask(private val ctx: Context) : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg params: String): String {
        var result = ""
        var connection: HttpURLConnection? = null
        var inputStream: InputStream? = null
        try {
            val url = URL(params[0])
            connection = url.openConnection() as HttpURLConnection
            connection.setConnectTimeout(10000)
            connection.setReadTimeout(10000)


            if (connection.getResponseCode() === HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream()

                var ch: Int
                val bos = ByteArrayOutputStream()
                ch = inputStream.read()
                while (ch != -1) {
                    bos.write(ch)
                    ch = inputStream.read()
                }

                result = String(bos.toByteArray())
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            if (connection != null) {
                connection.disconnect()
            }
        }

        return result
    }

    override fun onPostExecute(result: String) {
        val intentBrResult = Intent(FILTER_RESULT)
        intentBrResult.putExtra(KEY_RESULT, result)

        LocalBroadcastManager.getInstance(
            ctx).sendBroadcast(intentBrResult)
    }

    companion object {
        val FILTER_RESULT = "FILTER_RESULT"
        val KEY_RESULT = "KEY_RESULT"
    }
}
