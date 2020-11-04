package gsm.gsmnetindo.app_3s_checker.ui.main.result.detail

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
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
import gsm.gsmnetindo.app_3s_checker.data.network.response.barcode.Kuesioner
import gsm.gsmnetindo.app_3s_checker.internal.LocalDateTimeParser
import gsm.gsmnetindo.app_3s_checker.internal.ScopedFragment
import gsm.gsmnetindo.app_3s_checker.internal.Secret
import gsm.gsmnetindo.app_3s_checker.internal.glide.GlideApp
import gsm.gsmnetindo.app_3s_checker.internal.process.Likert
import gsm.gsmnetindo.app_3s_checker.ui.main.result.ResultViewModel
import gsm.gsmnetindo.app_3s_checker.ui.main.result.ResultViewModelFactory
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModel
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModelFactory
import kotlinx.android.synthetic.main.fragment_result_detail.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.TextStyle
import java.util.*

class DetailFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val resultViewModelFactory: ResultViewModelFactory by instance()
    private val accountViewModelFactory: AccountViewModelFactory by instance()
    private lateinit var resultViewModel: ResultViewModel
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resultViewModel =
            ViewModelProvider(this, resultViewModelFactory).get(ResultViewModel::class.java)
        accountViewModel =
            ViewModelProvider(this, accountViewModelFactory).get(AccountViewModel::class.java)
        return inflater.inflate(R.layout.fragment_result_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindData()
        status_refresh.setOnRefreshListener {
            bindData()
            displayAvatar()
        }
        status_refresh.isRefreshing = false
    }

    private fun bindData() = launch {
        status_refresh.isRefreshing = true
        try {
                resultViewModel.details.observe(viewLifecycleOwner, Observer {
                    when (it.status.status) {
                        0 -> {
                            status_status.text = "Sehat"
                            status_status.setTextColor(Color.parseColor("#32a86d"))
                            bg.setBackgroundColor(Color.parseColor("#32a86d"))
                        }
                        in 25..75 -> {
                            status_status.text = "Beresiko"
                            status_status.setTextColor(Color.parseColor("#dae600"))
                            bg.setBackgroundColor(Color.parseColor("#dae600"))
                        }
                        in 100..175 -> {
                            status_status.text = "Positif"
                            status_status.setTextColor(Color.parseColor("#e60000"))
                            bg.setBackgroundColor(Color.parseColor("#e60000"))
                        }
                    }
                    val url =
                        "${Secret.baseApi()}${Secret.apiVersion()}/${accountViewModel.getPhone().value}/avatar/${resultViewModel.barcode.value}"
                    glideUrl = GlideUrl(
                        url,
                        LazyHeaders.Builder()
                            .addHeader("Api-Key", Secret.apiKey())
                            .addHeader("Authorization", "Bearer ${accountViewModel.getToken().value}")
                            .build()
                    )
                    key = ObjectKey(it.account.avatar)
                    displayAvatar()
                    val lastUpdate = LocalDateTimeParser.utcToLocal(it.status.updatedAt)
                    val sixHoursLater = lastUpdate.plusHours(6)
                    status_last_check.text = dateTimeDisplay(lastUpdate.toLocalDateTime().toString())
                    status_expired_at.text = dateTimeDisplay(sixHoursLater.toLocalDateTime().toString())
                    status_nama.text = it.user.name
                    status_ktp.text = "${it.user.bornPlace}, ${it.user.bornDate}"

                    if (!it.status.verified) {
                        textView34.text = "*STATUS BELUM VERIFIKASI"
                        textView34.setTextColor(Color.parseColor("#e60000"))
                    } else {
                        textView34.text = "*STATUS TERVERIFIKASI\nKEABSAHAN DATA: ${it.status.accuracy}%"
                        textView34.setTextColor(Color.parseColor("#32a86d"))
                    }
                    status_refresh.isRefreshing = false
                })
        } catch (e: Exception) {
            Log.e("bindData", e.message, e)
        }
    }

    private lateinit var glideUrl: GlideUrl
    private lateinit var key: ObjectKey
    private fun displayAvatar(){
        GlideApp.with(requireActivity())
            .load(glideUrl)
            .signature(key)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    avatar_progress.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Tidak bisa memuat gambar",
                        Toast.LENGTH_SHORT
                    ).show()
                    return e != null
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    avatar_progress.visibility = View.GONE
                    target.apply { status_user_avatar.setImageDrawable(resource) }
                    return resource != null
                }

            })
            .into(status_user_avatar)
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