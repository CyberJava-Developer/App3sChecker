package gsm.gsmnetindo.app_3s_checker.smsgateway.networkchecker

import android.content.Context
import android.net.ConnectivityManager
import androidx.fragment.app.Fragment

class networkcheckerfragment {
    companion object{
        fun isNetwork(fragment: Fragment): Boolean {
            val conMan =
                fragment.activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return conMan.activeNetworkInfo != null && conMan.activeNetworkInfo
                .isConnected
        }
    }
}