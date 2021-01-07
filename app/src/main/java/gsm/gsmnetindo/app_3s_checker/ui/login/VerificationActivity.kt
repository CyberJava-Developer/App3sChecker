package gsm.gsmnetindo.app_3s_checker.ui.login

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.phone.SmsRetriever
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.internal.OTPReceiver
import gsm.gsmnetindo.app_3s_checker.internal.ScopedActivity
import gsm.gsmnetindo.app_3s_checker.smsgateway.SmsListener
import gsm.gsmnetindo.app_3s_checker.smsgateway.SmsReceiver
import gsm.gsmnetindo.app_3s_checker.ui.Intro.PERMISSION_ID
import gsm.gsmnetindo.app_3s_checker.ui.main.MainActivity
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModel
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModelFactory
import kotlinx.android.synthetic.main.activity_verificationlogin.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class VerificationActivity: ScopedActivity(), KodeinAware {
    override val kodein by closestKodein()

    private val accountViewModelFactory: AccountViewModelFactory by instance()
    private lateinit var accountViewModel: AccountViewModel

    private var intentFilter: IntentFilter? = null
    private var smsReceiver: OTPReceiver? = null

    private var codeServer: String = "server"
    private var codeSms: String = "sms"
    private var phone: String = ""
    private var jwt: String = ""
    private var role: Int = 0
    private var registered: Boolean = false
    private val match = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountViewModel = ViewModelProvider(this, accountViewModelFactory).get(AccountViewModel::class.java)
        setContentView(R.layout.activity_verificationlogin)
        ubahnomor.setOnClickListener { finishAndRemoveTask() }
        val phone = intent.getStringExtra("phoneNumber")
//        Toast.makeText(this, phone, Toast.LENGTH_LONG).show()
        AlertDialog.Builder(this).apply {
            setTitle("Izinkan aplikasi membaca SMS")
            setMessage("Agar aplikasi bisa menerima kode verifikasi, mohon izinkan aplikasi untuk membaca SMS")
            setPositiveButton("OK") { dialog, _ ->
//                requestSmsPermission(phone)
                dialog.dismiss()
            }
//            show()
        }
//        initReceiver()
//        login(phone)
//        resend_sms.setOnClickListener { login(phone) }
//        SmsReceiver.bindListener(this)
        initSmsListener(phone)
        initOTPReceiver()
    }
    private fun initSmsListener(phone: String){
        val client = SmsRetriever.getClient(this)
        client.startSmsRetriever()
        startVerification(phone)
    }
    private fun initOTPReceiver(){
        intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        smsReceiver = OTPReceiver()
        smsReceiver?.setOTPListener(object : OTPReceiver.OTPReceiveListener {
            override fun onOTPReceived(otp: String) {
                //showToast("OTP Received: $otp")
                codeSms = otp
                Log.d("sms code received", otp)
                animateReceivedCode(codeSms)
            }
        })
    }
    override fun onResume() {
        super.onResume()
        registerReceiver(smsReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(smsReceiver)
    }

    override fun onDestroy() {
        smsReceiver = null
        super.onDestroy()
    }

    private fun startVerification(phone: String) {
        initReceiver()
        login(phone)
        this.phone = phone
        number_txt.text = phone
        resend_sms.setOnClickListener { login(phone) }
//        SmsReceiver.bindListener(this)
    }

    private fun btnverify(){
        btn_verify.setOnClickListener {
            if (otp_view.text.toString() == codeServer){
                codeSms = codeServer
                match.postValue(matchCode())
                Toast.makeText(this, "Code verifikasi Sukses", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this, "Code verifikasi tidak valid", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun matchCode(): Boolean {
        Log.i("code", "sms: $codeSms - server:$codeServer")
        return codeServer == codeSms
    }
    private fun initReceiver(){
        match.observe(this, {
            if (it) {
                accountViewModel.setDeviceCredentials(phone, jwt, role)
                    Intent(Intent(this@VerificationActivity, MainActivity::class.java)).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(this)
                        finish()
                    }
            }
        })
    }
    private fun login(phone: String) = launch {
        startCountdown(60000)
        try {
            accountViewModel.login(phone).observe(this@VerificationActivity, {
                if (it.registered){
                    if (it.role < 2){
                        Toast.makeText(this@VerificationActivity, "Anda tidak memiliki izin untuk mengakses 3s Checker", Toast.LENGTH_LONG).show()
                        finishAndRemoveTask()
                        finish()
                        return@observe
                    } else {
                        registered = it.registered
                        codeServer = it.code
                        this@VerificationActivity.phone = it.phone
                        jwt = it.jwt
                        role = it.role

                        Log.d("otp code", it.code)
                        // ngakali tanpa sms langsung login
//                codeSms = it.code
                        btnverify()
//                match.postValue(matchCode())
                    }
                } else Toast.makeText(this@VerificationActivity, "Nomor anda tidak terdaftar dalam sistem", Toast.LENGTH_LONG).show()
            })
        } catch (e: Exception) {
            Log.e("login exception", e.message, e)
        }
    }
    private val retryTimer = object : CountDownTimer(60000, 1000){
        override fun onTick(millisUntilFinished: Long) {
            val sec = millisUntilFinished/1000
            timertxt.text = "$sec detik"
        }

        override fun onFinish() {
            resendEnabled(true)
            loginProcess(false, "")
            timertxt.visibility = View.GONE
            this.cancel()
        }

    }
    private fun startCountdown(milsec: Long) {
        timertxt.visibility = View.VISIBLE
        resendEnabled(false)
        loginProcess(true, "Menunggu sms verifikasi")
        retryTimer.start()
    }
    private fun resendEnabled(isIt: Boolean){
        resend_sms.apply {
            isEnabled = isIt
            isClickable = isIt
            visibility = if (isIt) View.VISIBLE
            else View.GONE
        }
    }
    private fun loginProcess(isIt: Boolean, hint: String) {
        if (isIt) {
            progress_verification.visibility = View.VISIBLE
            progress_hint.visibility = View.VISIBLE
            progress_hint.text = hint
        } else {
            progress_verification.visibility = View.GONE
            progress_hint.visibility = View.GONE
        }
    }
    override fun onBackPressed() {
        Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(this)
        }
    }
//    override fun messageReceived(message: String) {
//        codeSms = message
//        Log.d("code sms", codeSms)
//        otp_view.setText(codeSms)
//        Toast.makeText(this, "Code verifikasi Sukses", Toast.LENGTH_LONG).show()
//        match.postValue(matchCode())
//    }
    private fun animateReceivedCode(code: String) = launch {
        var temp = ""
        for (i in code.indices step 1 ){
            temp += i
            otp_view.setText(temp)
            delay(500)
        }
        match.postValue(matchCode())
    }
}