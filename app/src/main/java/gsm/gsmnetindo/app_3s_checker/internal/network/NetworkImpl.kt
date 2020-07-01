package gsm.gsmnetindo.app_3s_checker.internal.network

import android.content.Context
import android.net.ConnectivityManager

class NetworkImpl(
    private val context: Context
) : Network {
    override fun isOnline(): Boolean {
        val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}