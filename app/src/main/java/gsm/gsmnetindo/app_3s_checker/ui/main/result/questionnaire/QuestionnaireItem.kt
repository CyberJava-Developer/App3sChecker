package gsm.gsmnetindo.app_3s_checker.ui.main.result.questionnaire

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.data.network.response.barcode.History
import kotlinx.android.synthetic.main.item_questionnaire.history_coba

class QuestionnaireItem(
    private val history: History
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            history_coba.text = history.answer1
        }
    }

    override fun getLayout() = R.layout.item_questionnaire

}