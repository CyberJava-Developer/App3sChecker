package gsm.gsmnetindo.app_3s_checker.ui.login

import android.Manifest
import android.content.Intent
import android.os.*
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mukesh.OtpView
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.internal.ScopedActivity
import gsm.gsmnetindo.app_3s_checker.smsgateway.Api
import gsm.gsmnetindo.app_3s_checker.smsgateway.SmsListener
import gsm.gsmnetindo.app_3s_checker.smsgateway.SmsReceiver
import gsm.gsmnetindo.app_3s_checker.ui.main.MainActivity
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModel
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_verificationlogin.*
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception


private lateinit var phone: String
private lateinit var api: Api
class verificationclass : ScopedActivity(), SmsListener, KodeinAware {
    override val kodein by closestKodein()
    private val accountViewModelFactory: AccountViewModelFactory by instance()
    private lateinit var accountViewModel: AccountViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verificationlogin)
        accountViewModel = ViewModelProvider(this, accountViewModelFactory).get(AccountViewModel::class.java)
        checkRuntimePermission()
        initRetrofit()
        bindSmsReceiver()
        phone = intent.getStringExtra("number")
        val mText = findViewById<TextView>(R.id.number_txt)
        mText.text = phone
        timeractiviti(phone)
        val filtered =  phone.filter { c -> c.isDigit() }
        login(filtered)
        btnverifylogin()
        val otpView = findViewById<OtpView>(R.id.otp_view)
        otpView.setOtpCompletionListener { otp -> // do Stuff
            Log.d("onOtpCompleted=>", otp)
        }
        //ubah nomor
        changenumber()
    }

    private fun login(phonenumber: String) = launch {
        try {
            accountViewModel.login(phonenumber).observe(this@verificationclass, Observer {
                // for demo purpose, change role to be more than 1 (example: 2, 3, 4, up to 7)
                accountViewModel.getRolePref().apply {
                    if (this >= 2) {
                        Intent(this@verificationclass, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(this)
                            finish()
                        }
                    } else {
                        Toast.makeText(this@verificationclass, "anda tidak memiliki izin untuk login", Toast.LENGTH_LONG).show()
                    }
                }
            })

        } catch (e: HttpException) {
            Toast.makeText(this@verificationclass, e.message(), Toast.LENGTH_LONG).show()
        }
    }

    private fun changenumber(){
        ubahnomor.setOnClickListener{
            Toast.makeText(this, "Ubah nomor", Toast.LENGTH_LONG)
                .show()
            Intent(this@verificationclass, loginverification::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(this)
                finish()
            }
        }
    }

    private fun bindSmsReceiver() {
        SmsReceiver.bindListener(this)
    }

    private fun initRetrofit() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://server-otp-verifikation.herokuapp.com/api/")
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(Api::class.java)
    }

    private fun checkRuntimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.RECEIVE_SMS), 100)
        }
    }

    private fun sendVerificationCode(number: String) {
        var phoneNumber = number
        phoneNumber = "+62${number.substring(1)}"

        println(phoneNumber)
        if (phoneNumber.isBlank().not() || phoneNumber.isEmpty().not()) {
            api.sendOtp(phoneNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Toast.makeText(this, "sms verifikasi telah dikirim", Toast.LENGTH_LONG)
                            .show()
                    },
                    {
                        it.printStackTrace()
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                )
        }
    }

    private fun progress(b: Boolean) {
        if (b == true){
            progress_verification.visibility = View.VISIBLE
            progress_hint.visibility = View.VISIBLE
            resend_sms.isEnabled = false
            resend_sms.visibility = View.GONE
        }else{
            progress_verification.visibility = View.GONE
            progress_hint.visibility = View.GONE
            resend_sms.isEnabled = true
            resend_sms.visibility = View.VISIBLE
        }

    }

    private fun timeractiviti(phonenumber: String){
        progress(true)
        if (phonenumber.isNullOrBlank().not()){
            sendVerificationCode(phonenumber)
        }
        val textlefttimer = "0"
        val textrighttimer = "00:"
        val timer = object: CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timertxt.text = textrighttimer + millisUntilFinished/1000
                if(millisUntilFinished/1000 < 10){
                    timertxt.text = textrighttimer + textlefttimer + millisUntilFinished/1000
                }
            }

            override fun onFinish() {
                progress(false)
                timertxt.text = textrighttimer+textlefttimer+ 0
            }
        }
        timer.start()

        //resend activasi
        resend_sms.setOnClickListener {
            return@setOnClickListener timeractiviti(phonenumber)
        }
    }

    override fun messageReceived(message: String) {
        messageReceived1(message)
    }

    private fun messageReceived1(message: String) {
        Log.d(javaClass.simpleName, "message: $message")
        otp_view.setText(message.toString())
//        showProgressDialog()
        api.updateOtp(message)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
//                    hideProgressDialog()
                    val jsonObjectResponse = JSONObject(it.string())
                    if (jsonObjectResponse.getBoolean("success")) {
                        Toast.makeText(this, "Kode OTP benar", Toast.LENGTH_LONG)
                            .show()
                        login()
                    } else {
                        Toast.makeText(this, "Kode OTP salah", Toast.LENGTH_LONG)
                            .show()
                    }
                },
                {
                    it.printStackTrace()
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG)
                        .show()
                }
            )
    }

    private fun login() = launch {
        loginProcess(true, "login ke server")
        try {
            Intent(Intent(this@verificationclass, MainActivity::class.java)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(this)
                finish()
            }
        } catch (e: Exception){
            loginProcess(false, "menunggu sms verifikasi")

        }
    }

    private fun loginProcess(isIt: Boolean, hint: String) {
        if (isIt) {
            progress_verification.visibility = View.VISIBLE
            progress_hint.text = hint
        } else {
            progress_verification.visibility = View.GONE
            progress_hint.visibility = View.GONE
        }
    }

    private fun btnverifylogin(){
        btn_verify.setOnClickListener {
            val code: String = otp_view.text.toString()
            if (code.isEmpty() || code.length < 4) {
                otp_view.error = "Enter verification code"
                otp_view.requestFocus()
                return@setOnClickListener
            }
            login()
//            verifyCode(code);
            val textcode = code
            api.updateOtp(textcode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
//                        hideProgressDialog()
                        val jsonObjectResponse = JSONObject(it.string())
                        if (jsonObjectResponse.getBoolean("success")) {
                            Toast.makeText(this, "sms Kode OTP benar", Toast.LENGTH_LONG)
                                .show()
                            login()
                        } else {
                            Toast.makeText(this, "sms Kode OTP salah", Toast.LENGTH_LONG)
                                .show()
                        }
                    },
                    {
                        it.printStackTrace()
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                )
        }
    }

}