package gsm.gsmnetindo.app_3s_checker.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.internal.ScopedActivity
import gsm.gsmnetindo.app_3s_checker.smsgateway.SmsListener
import gsm.gsmnetindo.app_3s_checker.smsgateway.SmsReceiver
import gsm.gsmnetindo.app_3s_checker.ui.main.MainActivity
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModel
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModelFactory
import kotlinx.android.synthetic.main.activity_verificationlogin.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class VerificationActivity: ScopedActivity(), KodeinAware, SmsListener {
    override val kodein by closestKodein()

    private val accountViewModelFactory: AccountViewModelFactory by instance()
    private lateinit var accountViewModel: AccountViewModel

    private var codeServer: String = "server"
    private var codeSms: String = "sms"
    private val match = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountViewModel = ViewModelProvider(this, accountViewModelFactory).get(AccountViewModel::class.java)
        setContentView(R.layout.activity_verificationlogin)
        ubahnomor.setOnClickListener { finishAndRemoveTask() }
        val phone = intent.getStringExtra("phoneNumber")
        initReceiver()
        login(phone)
        resend_sms.setOnClickListener { login(phone) }
        SmsReceiver.bindListener(this)
    }
    private fun matchCode(): Boolean {
        Log.i("code", "sms: $codeSms - server:$codeServer")
        return codeServer == codeSms
    }
    private fun initReceiver(){
        match.observe(this, Observer {
            if (it) {
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
            accountViewModel.login(phone).observe(this@VerificationActivity, Observer {
                codeServer= it.code

                // ngakali tanpa sms langsung login
                codeSms = it.code

                match.postValue(matchCode())
            })
        } catch (e: Exception) {
            Log.e("login exception", e.message, e)
        }
    }
    private fun startCountdown(milsec: Long) {
        timertxt.visibility = View.VISIBLE
        resendEnabled(false)
        loginProcess(true, "menunggu sms verifikasi")
        object : CountDownTimer(milsec, 1000){
            override fun onTick(millisUntilFinished: Long) {
                val sec = millisUntilFinished/1000
                timertxt.text = "$sec detik"
            }

            override fun onFinish() {
                resendEnabled(true)
                loginProcess(false, "")
                timertxt.visibility = View.GONE
            }
        }.start()
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
    override fun messageReceived(message: String) {
        codeSms = message
        match.postValue(matchCode())
    }
}