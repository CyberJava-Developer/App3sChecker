package gsm.gsmnetindo.app_3s_checker.smsgateway.networkchecker

import android.content.Context
import android.net.ConnectivityManager

class isNetworkAvailable {
    companion object{
        fun isNetwork(context: Context): Boolean {
            val conMan =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return conMan.activeNetworkInfo != null && conMan.activeNetworkInfo
                    .isConnected
        }
    }
}

