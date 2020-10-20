package gsm.gsmnetindo.app_3s_checker.ui.main.result.questionnaire

import android.util.Log
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.data.network.response.barcode.History
import kotlinx.android.synthetic.main.item_questionnaire.*

class QuestionnaireItem(
    private val history: History
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
//            history_coba.text = history.answer1
            Log.d("kuesionare","${history.answer1}, ${history.answer2}, ${history.answer3}, ${history.answer4}")
            when (history.answer1) {
                "tidak" -> {
                    kuesioner_group_1.check(R.id.tidak1)
                }
                else -> {
                    kuesioner_group_1.check(R.id.iya1)
                }
            }
            when (history.answer2) {
                "tidak" -> {
                    kuesioner_group_2.check(R.id.tidak2)
                }
                else -> {
                    kuesioner_group_2.check(R.id.iya2)
                }
            }
            when (history.answer3) {
                "tidak" -> {
                    kuesioner_group_3.check(R.id.tidak3)
                }
                "ragu" -> {
                    kuesioner_group_3.check(R.id.ragu3)
                }
                "iya" -> {
                    kuesioner_group_1.check(R.id.iya3)
                }
                else -> {
                }
            }
            when (history.answer4) {
                "tidak" -> {
                    kuesioner_group_4.check(R.id.tidak4)
                }
                "iya" -> {
                    kuesioner_group_1.check(R.id.iya4)
                }
                else -> {
                    kuesioner_group_4.check(0)
                }
            }
        }
    }

    override fun getLayout() = R.layout.item_questionnaire

}