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
import gsm.gsmnetindo.app_3s_checker.ui.main.result.ResultActivity
import gsm.gsmnetindo.app_3s_checker.ui.main.result.ResultViewModel
import gsm.gsmnetindo.app_3s_checker.ui.main.result.ResultViewModelFactory
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModel
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModelFactory
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.BarcodeViewModel
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.BarcodeViewModelFactory
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
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
        barcodeViewModel = ViewModelProvider(this, barcodeViewModelFactory).get(BarcodeViewModel::class.java)
        accountViewModel = ViewModelProvider(this, accountViewModelFactory).get(AccountViewModel::class.java)
        resultViewModel = ViewModelProvider(this, resultViewModelFactory).get(ResultViewModel::class.java)
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
    fun showpopup(code: String, user: BarcodeDetailResponse) {
        val inflater: LayoutInflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.popup_qrcode_scanner, null)

        val avatar = dialogView.findViewById<ImageView>(R.id.dialog_avatar)
        val status = dialogView.findViewById<TextView>(R.id.dialog_status)
        val name = dialogView.findViewById<TextView>(R.id.dialog_name)
        val lastCheck = dialogView.findViewById<TextView>(R.id.dialog_last)
        val backg = dialogView.findViewById<LinearLayout>(R.id.backgr)

        val url = "${Secret.baseApi()}${Secret.apiVersion()}/${accountViewModel.getPhone().value}/avatar/$code"
        val glideUrl = GlideUrl(
            url,
            LazyHeaders.Builder()
                .addHeader("Api-Key", Secret.apiKey())
                .addHeader("Authorization", "Bearer ${accountViewModel.getToken().value}")
                .build()
        )
        val key = ObjectKey(user.account.avatar)
        GlideApp.with(dialogView)
            .load(glideUrl)
            .signature(key)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Toast.makeText(requireContext(), "Tidak bisa memuat gambar", Toast.LENGTH_SHORT).show()
                    return e != null
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    target.apply { avatar.setImageDrawable(resource) }
                    return resource != null
                }

            })
            .into(avatar)

        when(user.status.status){
            "negative"->{
                status.text = "Sehat"
                backg.setBackgroundColor(Color.parseColor("#6DBEAF"))
            }
            "odp"->{
                status.text = "Dalam Pengawasan"
                backg.setBackgroundColor(Color.parseColor("#E4B761"))
            }
            "pdp"->{
                status.text = "Pasien Pengawasan"
                backg.setBackgroundColor(Color.parseColor("#E4B761"))
            }
            "positive"->{
                status.text = "Positif Corona"
                backg.setBackgroundColor(Color.parseColor("#E06E71"))
            }
        }
        name.text = user.user.name
        val last = LocalDateTimeParser.utcToLocal(user.status.updatedAt)
        val day = last.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val month = last.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        lastCheck.text = "Terakhir periksa:\n$day, ${last.dayOfMonth} $month ${last.year} ${last.toLocalTime()}"

        val txtcloses = dialogView.findViewById<TextView>(R.id.txtclose)

        val btncloses: Button = dialogView.findViewById(R.id.btnclose)
        btncloses.setOnClickListener {
            alertDialog.dismiss()
            codeScanner.startPreview()
        }

        txtcloses.setOnClickListener {
            alertDialog.dismiss()
            codeScanner.startPreview()
        }

        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        dialogBuilder.setOnDismissListener { }
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create();
        alertDialog.window!!.attributes.windowAnimations =
            R.style.ShapeAppearanceOverlay_MaterialComponents_MaterialCalendar_Window_Fullscreen
        alertDialog.show()
    }
}