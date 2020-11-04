package gsm.gsmnetindo.app_3s_checker.ui.main.result.questionnaire

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.qtalk.recyclerviewfastscroller.RecyclerViewFastScroller
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.data.network.response.barcode.History
import gsm.gsmnetindo.app_3s_checker.internal.DateTimeParser
import kotlinx.android.synthetic.main.item_questionnaire.*
import org.threeten.bp.format.TextStyle
import java.util.*

class QuestionnaireItem(
    private val context: Context,
    private val latestId: Int,
    private val questionnaireEntry: History
) : Item(), RecyclerViewFastScroller.OnPopupViewUpdate {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            this.containerView.animation = AnimationUtils.loadAnimation(context, R.anim.fade_transtition)
            this.containerView.animation = AnimationUtils.loadAnimation(context, R.anim.fade_scale_transtition)
            val dateTime = DateTimeParser().parse(questionnaireEntry.createdAt)
            val day = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ID"))
            val month = dateTime.month.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ID"))
            questionnaire_date.text = "$day, ${dateTime.dayOfMonth} $month ${dateTime.year} ${dateTime.toLocalTime()}"
            setStatus(questionnaireEntry.verified)
            questionnaire_detail.visibility = if (latestId == questionnaireEntry.id){
                View.VISIBLE
            } else {
                View.GONE
            }
            questionnaire_detail.setOnClickListener {
                val question = "${questionnaireEntry.answer1},${questionnaireEntry.answer2}," +
                        "${questionnaireEntry.answer3},${questionnaireEntry.answer4}"
                Log.d("Questionare", "$question")
                Intent(context, QuestionnaireDetailActivity::class.java).apply {
                    putExtra("questId", questionnaireEntry.id)
                    putExtra("questionare", question)
                    context.startActivity(this)
                }
            }
        }
    }

    override fun getLayout() = R.layout.item_questionnaire
    private fun ViewHolder.setStatus(verified: Boolean){
        if (verified) {
            questionnaire_status.text = "Terverifikasi"
        } else {
            questionnaire_status.text = "Belum terverifikasi"
        }
    }

    override fun onUpdate(position: Int, popupTextView: TextView) {
        popupTextView.setBackgroundColor(Color.parseColor("#2D2F31"))
    }
}