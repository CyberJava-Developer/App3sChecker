package gsm.gsmnetindo.app_3s_checker.ui.login

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import gsm.gsmnetindo.app_3s_checker.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.i("Activity Started", "Login Activity")
        initUI()
        phone_login.addTextChangedListener(PhoneNumberFormattingTextWatcher("ID"))
//        smsPermission()
    }
    private fun initUI(){
        initValidator()
        next_login2.setOnClickListener { v ->
            // no memet 0838-4761-6748
            filterPhone()
        }
    }
    private fun filterPhone(){
//        login_progress.visibility = View.VISIBLE
        val number : String = phone_login.text.toString()
        if (number.isEmpty() || number.length < 9) {
            phone_login.error = "mohon masukan nomor telpon yang valid"
            phone_login.requestFocus()
            return
        }
        val phone = number.filter { c -> c.isDigit() }
        //accountViewModel._phoneMemory.postValue(phone)

        val phoneNumber = if (phone.startsWith("62")){
            "62${phone.substring(2)}"
        } else if (phone.startsWith("8")){
            "62$phone"
        } else {
            "62${phone.substring(1)}"
        }
        verifySms(phoneNumber)
    }
    private fun verifySms(p: String){
        val phone = "+$p"
        AlertDialog.Builder(this).apply {
            setTitle("Apakah nomor $phone sudah benar?")
            setMessage("Kami akan mengirim kode sms verifikasi ke nomor $phone")
            setPositiveButton("OK") { _, _ ->
                Intent(this@LoginActivity, VerificationActivity::class.java).apply {
                    putExtra("phoneNumber", p)
                    startActivity(this)
                }
            }
            setNegativeButton("Ubah") { dialog, _ ->
                phone_login.setText("")
                dialog.dismiss()
            }
            show()
        }
    }
    private fun initValidator(){
        phone_login.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    //Log.d("phone editText", s.length.toString())
                    if (s.length >= 10) {
                        next_login2.isEnabled = true
                        next_login2.isClickable = true
                        next_login2.setBackgroundResource(R.drawable.buttonui)
                    } else {
                        next_login2.isEnabled = false
                        next_login2.isClickable = false
                        next_login2.setBackgroundColor(Color.parseColor("#eeeeee"))
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
    private fun smsPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(arrayOf(Manifest.permission.RECEIVE_SMS),100)
        }
    }
}