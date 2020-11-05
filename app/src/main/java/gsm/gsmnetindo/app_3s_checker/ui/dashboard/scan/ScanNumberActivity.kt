package gsm.gsmnetindo.app_3s_checker.ui.dashboard.scan

import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.data.network.response.barcode.BarcodeDetailResponse
import gsm.gsmnetindo.app_3s_checker.internal.ScopedActivity
import gsm.gsmnetindo.app_3s_checker.ui.main.result.ResultActivity
import gsm.gsmnetindo.app_3s_checker.ui.main.result.ResultViewModel
import gsm.gsmnetindo.app_3s_checker.ui.main.result.ResultViewModelFactory
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.BarcodeViewModel
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.BarcodeViewModelFactory
import kotlinx.android.synthetic.main.activity_scan_number.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import retrofit2.HttpException
import java.net.SocketTimeoutException

class ScanNumberActivity: ScopedActivity(), KodeinAware {
    override val kodein by closestKodein()
    private val barcodeViewModelFactory: BarcodeViewModelFactory by instance()
    private lateinit var barcodeViewModel: BarcodeViewModel
    private val resultViewModelFactory: ResultViewModelFactory by instance()
    private lateinit var resultViewModel: ResultViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        barcodeViewModel = ViewModelProvider(this, barcodeViewModelFactory).get(BarcodeViewModel::class.java)
        resultViewModel = ViewModelProvider(this, resultViewModelFactory).get(ResultViewModel::class.java)
        setContentView(R.layout.activity_scan_number)
        number_phone_edittext.apply {
            addTextChangedListener(PhoneNumberFormattingTextWatcher("ID"))
            setOnKeyListener(object : View.OnKeyListener {
                override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                    if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                        number_phone_edittext.onKeyUp(keyCode, event)
                        Toast.makeText(this@ScanNumberActivity, "Scanning result for ${getFormattedPhone()}", Toast.LENGTH_SHORT)
                            .show()
                        scan(getFormattedPhone())
                        return true
                    }
                    return false
                }
            })
        }
    }
    private fun getFormattedPhone(): String {
        val unfiltered = number_phone_edittext.text.toString()
        val filtered = unfiltered.filter { c -> c.isDigit() }

        return if (filtered.startsWith("62")) {
            "62${filtered.substring(2)}"
        } else if (filtered.startsWith("8")) {
            "62$filtered"
        } else {
            "62${filtered.substring(1)}"
        }
    }
    private fun scan(code: String) = launch {
        try {
            barcodeViewModel.scan(code).observe(this@ScanNumberActivity, {
                showResult(code, it)
                number_phone_edittext.setText("")
            })
        } catch (e: Exception){
            Log.e("scan phone", e.message, e)
            number_phone_edittext.setText("")
            when(e){
                is HttpException -> {
                    if (e.code() == 404) Toast.makeText(this@ScanNumberActivity, "Nomor telepon tidak ditemukan", Toast.LENGTH_LONG).show()
                    else Toast.makeText(this@ScanNumberActivity, e.message, Toast.LENGTH_LONG).show()
                }
                is SocketTimeoutException -> {
                    Toast.makeText(this@ScanNumberActivity, "Tidak bisa tersambung dengan server", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this@ScanNumberActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun showResult(code: String, barcodeDetailResponse: BarcodeDetailResponse){
        resultViewModel.setDetail(code, barcodeDetailResponse)
        Intent(this, ResultActivity::class.java).apply {
            putExtra(ResultActivity.INTENT_EXTRA_NAME, code)
            startActivity(this)
        }
    }
}