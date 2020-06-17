package gsm.gsmnetindo.app_3s_checker.ui.dashboard

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import gsm.gsmnetindo.app_3s_checker.R

class Fragment_QRcode : Fragment() {

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
                showpopup()
            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
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

     fun showpopup(){
         val inflater: LayoutInflater = this.layoutInflater
         val dialogView: View = inflater.inflate(R.layout.popup_qrcode_scanner, null)

         val txtcloses = dialogView.findViewById<TextView>(R.id.txtclose)
         val btncloses: Button = dialogView.findViewById(R.id.btnclose)
         btncloses.setOnClickListener {
             alertDialog.dismiss()
         }

         txtcloses.setOnClickListener{
             alertDialog.dismiss()
         }

         val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context!!)
         dialogBuilder.setOnDismissListener { }
         dialogBuilder.setView(dialogView)

         alertDialog = dialogBuilder.create();
         alertDialog.window!!.attributes.windowAnimations = R.style.ShapeAppearanceOverlay_MaterialComponents_MaterialCalendar_Window_Fullscreen
         alertDialog.show()
    }
}