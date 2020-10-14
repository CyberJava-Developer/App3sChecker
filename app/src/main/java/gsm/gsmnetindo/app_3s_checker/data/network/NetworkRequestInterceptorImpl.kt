package gsm.gsmnetindo.app_3s_checker.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import gsm.gsmnetindo.app_3s_checker.data.preference.user.UserManager
import gsm.gsmnetindo.app_3s_checker.internal.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response

private const val TAG = "NetworkResInterceptor"

@Suppress("DEPRECATION")
class NetworkRequestInterceptorImpl(
    context: Context,
    private val userManager: UserManager
) : NetworkRequestInterceptor {
    private  val appContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline())
            run()

        val jwt = userManager.getTokenPref()
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer $jwt")
            .build()

        return chain.proceed(request)
    }

    private fun isOnline(): Boolean {
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    var mHandler: Handler? = null
    private fun run() {
        Looper.prepare()
        mHandler = object : Handler() {

            override fun handleMessage(msg: Message) {
                msg.apply { val mes = "internet tidak aktif"
                    msg.toString()
                    Log.d("$msg", mes)
                }
            }
        }
        Looper.loop()
    }
}