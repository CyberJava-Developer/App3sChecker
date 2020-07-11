package gsm.gsmnetindo.app_3s_checker.ui.main.result.detail

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.internal.LocalDateTimeParser
import gsm.gsmnetindo.app_3s_checker.internal.ScopedFragment
import gsm.gsmnetindo.app_3s_checker.internal.Secret
import gsm.gsmnetindo.app_3s_checker.internal.glide.GlideApp
import gsm.gsmnetindo.app_3s_checker.ui.main.result.ResultViewModel
import gsm.gsmnetindo.app_3s_checker.ui.main.result.ResultViewModelFactory
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModel
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModelFactory
import kotlinx.android.synthetic.main.fragment_result_detail.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.TextStyle
import java.util.*

class DetailFragment: ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val resultViewModelFactory by instance<ResultViewModelFactory>()
    private val accountViewModelFactory by instance<AccountViewModelFactory>()
    private lateinit var resultViewModel: ResultViewModel
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resultViewModel = ViewModelProvider(this, resultViewModelFactory).get(ResultViewModel::class.java)
        accountViewModel = ViewModelProvider(this, accountViewModelFactory).get(AccountViewModel::class.java)
        return inflater.inflate(R.layout.fragment_result_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        resultViewModel.details.observe(viewLifecycleOwner, Observer {
            when(it.status.status){
                "negative"->{
                    detail_status.text = "Status Sehat"
                    detail_status.setTextColor(Color.parseColor("#32a86d"))
                }
                "odp"->{
                    detail_status.text = "Status"
                    detail_status.setTextColor(Color.parseColor("#dae600"))
                }
                "pdp" -> {
                    detail_status.text = "Pasien Perawatan"
                    detail_status.setTextColor(Color.parseColor("#e67e00"))
                }
                "positive" -> {
                    detail_status.text = "Status Positif"
                    detail_status.setTextColor(Color.parseColor("#e60000"))
                }
                else -> {
                    detail_status.text = "Tidak Terverifikasi"
                    detail_status.setTextColor(Color.parseColor("#00c5e3"))
                }
            }
            val urls = "http://my3s.local/checker/v1/6281249499076/avatar/xJ6UMTh3lokKiHvPI78my2ZprcNf5tV9"
//            resultViewModel.setBarcode("xJ6UMTh3lokKiHvPI78my2ZprcNf5tV9")


            val url = "${Secret.baseApi()}${Secret.apiVersion()}/${accountViewModel.getPhone().value}/avatar/${resultViewModel.barcode.value}"
            val glideUrl = GlideUrl(
                url,
                LazyHeaders.Builder()
                    .addHeader("Api-Key", Secret.apiKey())
                    .addHeader("Authorization", "Bearer ${accountViewModel.getToken().value}")
                    .build()
            )
            val key = ObjectKey(it.account.avatar)
            GlideApp.with(this)
                .load(glideUrl)
                .signature(key)
                .listener(object : RequestListener<Drawable>{
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
                        target.apply { detail_avatar.setImageDrawable(resource) }
                        return resource != null
                    }

                })
                .into(detail_avatar)
            val lastUpdate = LocalDateTimeParser.utcToLocal(it.status.updatedAt)
            val sixHoursLater = lastUpdate.plusHours(6)
            detail_last_check.text = "Terakhir periksa:\n" + dateTimeDisplay(lastUpdate.toLocalDateTime().toString())
            detail_expired_at.text = "Berlaku sampai:\n" + dateTimeDisplay(sixHoursLater.toLocalDateTime().toString())
            detail_name.text = it.user.name
            detail_birth.text = "${it.user.bornPlace}, ${it.user.bornDate}"
        })
    }
    private fun dateTimeDisplay(dateTime: String): String {
        val rawDateTime = LocalDateTime.parse(dateTime)
        val zonedDateTime = ZonedDateTime.of(
            rawDateTime.toLocalDate(),
            rawDateTime.toLocalTime(),
            ZoneId.systemDefault()
        )
        val day = zonedDateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val month = zonedDateTime.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
//        val day =
//            zonedDateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ID"))
//        val month = zonedDateTime.month.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ID"))
        return "$day, ${zonedDateTime.dayOfMonth} $month ${zonedDateTime.year} ${zonedDateTime.toLocalTime()}"
    }
}