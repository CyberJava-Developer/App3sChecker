package gsm.gsmnetindo.app_3s_checker.ui.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import gsm.gsmnetindo.app_3s_checker.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.system.exitProcess


class loginverification: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)

        next_login.setOnClickListener{
            val msg: String = phone_login.text.toString()

            //check if the EditText have values or not
            if(msg.trim().isNotEmpty()) {
                val intent = Intent(this, verificationlogin::class.java)
                val mEditText = findViewById<EditText>(R.id.phone_login)
                val str = mEditText.text.toString()
                if (str.startsWith("62")){
                    val phonenmber = "62${str.drop(2)}"
                    Toast.makeText(applicationContext, "pakek 62  $phonenmber", Toast.LENGTH_SHORT).show()
                    intent.putExtra("number", phonenmber)
                    startActivity(intent)
                    finish()
                }
                else if (str.startsWith("0")){
                    val phonenmber = "62${str.drop(1)}"
                    Toast.makeText(applicationContext, "pakek 08  $phonenmber", Toast.LENGTH_SHORT).show()
                    intent.putExtra("number", phonenmber)
                    startActivity(intent)
                    finish()
                }

            }else{
                alerndialog()
            }
        }

        next_login2.setOnClickListener {
            val intent = Intent(this, verificationlogin::class.java)
            val mEditText = findViewById<EditText>(R.id.phone_login)
            val str = mEditText.text.toString()
            if (str.startsWith("62")){
                val phonenmber = "62${str.substring(2)}"
                Toast.makeText(applicationContext, "pakek 62  $phonenmber", Toast.LENGTH_SHORT).show()
                intent.putExtra("number", phonenmber)
                startActivity(intent)
                finish()
            }
            else if (str.startsWith("08")){
                val phonenmber = "62${str.substring(1)}"
                Toast.makeText(applicationContext, "pakek 08  $phonenmber", Toast.LENGTH_SHORT).show()
                intent.putExtra("number", phonenmber)
                startActivity(intent)
                finish()
            }else{
                alerndialog()
            }
        }
    }

    fun alerndialog(){
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Error")
            .setContentText("Mohon isi nomor ponsel dengan benar")
            .setConfirmText("OK")
            .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation()
            }
            .show()
    }


}