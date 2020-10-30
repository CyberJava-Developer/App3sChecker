package gsm.gsmnetindo.app_3s_checker.ui.dashboard.home

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.data.db.entity.FeedItem
import gsm.gsmnetindo.app_3s_checker.internal.DateTimeParser
import gsm.gsmnetindo.app_3s_checker.internal.LocalDateTimeParser
import gsm.gsmnetindo.app_3s_checker.internal.glide.GlideApp
import kotlinx.android.synthetic.main.item_card_feed.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.TextStyle
import java.util.*

class FeedCardItem(
    private val context: Context,
    private val feedItem: FeedItem
): Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            val create = LocalDateTimeParser.utcToLocal(feedItem.createdAt)
            val formateCreate = dateTimeDisplay(
                create.toLocalDateTime().toString()
            )
            feed_title.text = feedItem.title
            tagl.text = formateCreate
            fetchImage(feedItem.image)
            feed_container.setOnClickListener {
                Log.i("open browser", feedItem.link)
                ContextCompat.startActivity(context, Intent(Intent.ACTION_VIEW, Uri.parse(feedItem.link)), null)
            }
        }
    }

    override fun getLayout() = R.layout.item_card_feed
    private fun ViewHolder.fetchImage(imageUrl: String){
        GlideApp.with(this.containerView)
            .load(imageUrl)
            .listener(object : RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return e != null
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    target.apply { feed_image.setImageDrawable(resource) }
                    return resource != null
                }

            })
            .into(feed_image)
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