package gsm.gsmnetindo.app_3s_checker.ui.dashboard

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.data.network.response.barcode.BarcodeDetailResponse
import gsm.gsmnetindo.app_3s_checker.internal.LocalDateTimeParser
import gsm.gsmnetindo.app_3s_checker.internal.ScopedFragment
import gsm.gsmnetindo.app_3s_checker.internal.Secret
import gsm.gsmnetindo.app_3s_checker.internal.glide.GlideApp
import gsm.gsmnetindo.app_3s_checker.ui.dashboard.scan.scane_number
import gsm.gsmnetindo.app_3s_checker.ui.main.MainActivity
import gsm.gsmnetindo.app_3s_checker.ui.main.result.*
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModel
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModelFactory
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.BarcodeViewModel
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.BarcodeViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_qrcode.*
import kotlinx.android.synthetic.main.popup_qrcode_scanner.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.format.TextStyle
import retrofit2.HttpException
import java.util.*

class Fragment_QRcode : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val barcodeViewModelFactory: BarcodeViewModelFactory by instance()
    private lateinit var barcodeViewModel: BarcodeViewModel
    private val accountViewModelFactory by instance<AccountViewModelFactory>()
    private lateinit var accountViewModel: AccountViewModel
    private val resultViewModelFactory by instance<ResultViewModelFactory>()
    private lateinit var resultViewModel: ResultViewModel

    private lateinit var codeScanner: CodeScanner
    private lateinit var alertDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.activity_qrcode, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                //Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
//                showpopup()
                if (it.text.length == 32) {
                    scan(it.text)
                } else {
                    Toast.makeText(requireContext(), "Barcode tidak dikenali", Toast.LENGTH_SHORT).show()
                    codeScanner.startPreview()
                }
                Log.d("code length", "${it.text.length}")
            }
        }
        scanner_flip.setOnClickListener {
            codeScanner.stopPreview()
            when(codeScanner.camera) {
                1 -> {
                    codeScanner.camera = CodeScanner.CAMERA_BACK
                }
                0 -> {
                    codeScanner.camera = CodeScanner.CAMERA_FRONT
                }
            }
            codeScanner.startPreview()
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        search_number.setOnClickListener{
//            Intent(Intent(context, scane_number::class.java)).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(this)
//                }
            val intent = Intent(context, scane_number::class.java)
            startActivity(intent)
        }

        barcodeViewModel = ViewModelProvider(this, barcodeViewModelFactory).get(BarcodeViewModel::class.java)
        accountViewModel = ViewModelProvider(this, accountViewModelFactory).get(AccountViewModel::class.java)
        resultViewModel = ViewModelProvider(this, resultViewModelFactory).get(ResultViewModel::class.java)

        when(accountViewModel.getRolePref()) {
            2 -> { search_layout.visibility = View.GONE }
            3 -> { search_layout.visibility = View.GONE }
            4 -> { search_layout.visibility = View.GONE }
            5 or 7 -> {search_layout.visibility = View.VISIBLE}
            else -> { search_layout.visibility = View.GONE }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
    private fun scan(code: String) = launch {
        try {
            barcodeViewModel.scan(code).observe(viewLifecycleOwner, Observer {
//                showpopup(code, it)
                showResult(code, it)
            })
        } catch (e: HttpException) {
            Toast.makeText(requireContext(), e.message(), Toast.LENGTH_LONG).show()
            codeScanner.startPreview()
        }
    }
    private fun showResult(code: String, user: BarcodeDetailResponse){
        resultViewModel.setDetail(code, user)
        Intent(requireContext(), ResultActivity::class.java).apply {
            putExtra("code", code)

//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(this)
            }
    }
}