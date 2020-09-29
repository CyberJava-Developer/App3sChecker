package gsm.gsmnetindo.app_3s_checker.ui.dashboard

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gsm.gsmnetindo.app_3s_checker.BuildConfig
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
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
import gsm.gsmnetindo.app_3s_checker.ui.login.LoginActivity
import gsm.gsmnetindo.app_3s_checker.ui.login.loginverification
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModel
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModelFactory
import kotlinx.android.synthetic.main.activity_akun.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.TextStyle
import java.util.*

class Fragment_akun : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val accountViewModelFactory: AccountViewModelFactory by instance()
    private lateinit var accountViewModel: AccountViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_akun, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        progressktp.visibility = View.GONE
        accountViewModel =
            ViewModelProvider(this, accountViewModelFactory).get(AccountViewModel::class.java)
        account_button_logout.setOnClickListener {
            logout()
        }
        bindUI()
        syncDetail()
    }

    private fun logout() {
        accountViewModel.logout()
        if (activity != null) {
            Intent(activity, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(this)
                requireActivity().finish()
            }
        }
    }

    private fun bindUI() {
        try {
            accountViewModel.detail.observe(viewLifecycleOwner) {
                Log.i("fragment detail", "$it")
                txtname.text = it.detail.name
                nomortelepon.text = "+${it.account.phone}"
                txtlahir.text = "${it.detail.bornDate}"
                tempat_lahir.text = "${it.detail.bornPlace}"
                loadAvatar(ObjectKey(it.account.avatar))

                val lastUpdate = LocalDateTimeParser.utcToLocal(it.status.updatedAt)

                val sixHoursLater = lastUpdate.plusHours(24L)
                Log.d("date time", lastUpdate.toString())
                if (lastUpdate != null) {
                    last.text =
                        "Terakhir periksa:\n" + dateTimeDisplay(
                            lastUpdate.toLocalDateTime().toString()
                        )
                }
                if (sixHoursLater != null) {
                    exp.text = "Berlaku sampai:\n" + dateTimeDisplay(
                        sixHoursLater.toLocalDateTime().toString()
                    )
                }
                ZonedDateTime.now(ZoneId.systemDefault()).isAfter(sixHoursLater).apply {
                    val now = ZonedDateTime.now(ZoneId.systemDefault())
                    Log.d("is 6 hours", "$now - $sixHoursLater = $this")
//                    update.isCheckable = this
//                    update.isEnabled = this
                }
            }
        } catch (e: Exception){
            Log.e("fragment akun", e.message, e)
        }

    }

    private fun syncDetail() = launch {
        try {
            accountViewModel.syncDetail()
        } catch (e: Exception) {
            Log.e("fetchDetail", e.message, e)
        }
    }

    private fun loadAvatar(key: ObjectKey) {
        val url =
            "${Secret.baseApi()}${Secret.apiVersion()}/${accountViewModel.getPhonePref()}/avatar.jpg"
        val glideUrl = GlideUrl(
            url,
            LazyHeaders.Builder()
                .addHeader("Api-Key", Secret.apiKey())
                .addHeader("Authorization", "Bearer ${accountViewModel.getToken().value}")
                .build()
        )
        GlideApp.with(this)
            .load(glideUrl)
            .signature(key)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressktp.visibility = View.GONE
                    return e != null
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressktp.visibility = View.GONE
                    target.apply { account_avatar.setImageDrawable(resource) }
                    return resource != null
                }

            })
            .into(account_avatar)
    }

    private fun dateTimeDisplay(dateTime: String): String {
        val rawDateTime = LocalDateTime.parse(dateTime)
        val zonedDateTime = ZonedDateTime.of(
            rawDateTime.toLocalDate(),
            rawDateTime.toLocalTime(),
            ZoneId.systemDefault()
        )
        val day =
            zonedDateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ID"))
        val month = zonedDateTime.month.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ID"))
        return "$day, ${zonedDateTime.dayOfMonth} $month ${zonedDateTime.year} ${zonedDateTime.toLocalTime()}"
    }
}