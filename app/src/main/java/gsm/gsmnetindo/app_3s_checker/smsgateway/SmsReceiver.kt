package gsm.gsmnetindo.app_3s_checker.smsgateway

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import android.util.Log

class SmsReceiver : BroadcastReceiver() {

    companion object {
        private var smsListener: SmsListener? = null

        fun bindListener(smsListener: SmsListener) {
            Companion.smsListener = smsListener
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val extras = intent?.extras
        val pdus = extras?.get("pdus") as Array<*>
        for (item in pdus) {
            val smsMessage: SmsMessage
            smsMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val format = extras.getString("format")
                SmsMessage.createFromPdu(item as ByteArray, format)
            } else {
                SmsMessage.createFromPdu(item as ByteArray)
            }
            val message = smsMessage.messageBody
            val numbers = message.filter { c -> c.isDigit() }
            val code = numbers.take(4)
            Log.d("SmsReceiver", code)
            smsListener?.messageReceived(code)
        }
    }

}