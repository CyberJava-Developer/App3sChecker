package gsm.gsmnetindo.app_3s_checker.ui.main.result.questionnaire

import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.data.network.body.DataPostQuestionnaire
import gsm.gsmnetindo.app_3s_checker.data.network.response.barcode.History
import gsm.gsmnetindo.app_3s_checker.internal.LocalDateTimeParser
import gsm.gsmnetindo.app_3s_checker.internal.ScopedActivity
import gsm.gsmnetindo.app_3s_checker.internal.process.Likert
import gsm.gsmnetindo.app_3s_checker.internal.process.LikertResult
import gsm.gsmnetindo.app_3s_checker.ui.main.result.ResultViewModel
import gsm.gsmnetindo.app_3s_checker.ui.main.result.ResultViewModelFactory
import kotlinx.android.synthetic.main.activity_lihat_questionare.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.net.SocketTimeoutException
import kotlin.properties.Delegates

class QuestionnaireDetailActivity : ScopedActivity(), KodeinAware {

    override val kodein by closestKodein()
    private val resultViewModelFactory: ResultViewModelFactory by instance()
    private lateinit var resultViewModel: ResultViewModel

    private var id by Delegates.notNull<Int>()
    private lateinit var likert: LikertResult
    private lateinit var itemString: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultViewModel = ViewModelProvider(this, resultViewModelFactory).get(ResultViewModel::class.java)
        setContentView(R.layout.activity_lihat_questionare)
        id = intent.getIntExtra("questId", 0)
        val question = intent.getStringExtra("questionare")
        itemString = stringToWords(question).toMutableList()
        calculate()
        Log.d("dari item", itemString[0])
        when (itemString[0]) {
            "tidak" -> {
                kuesioner_group_1.check(R.id.tidak1)
            }
            "iya" ->{
                kuesioner_group_1.check(R.id.iya1)
            }
            else -> {

            }
        }
        when (itemString[1]) {
            "tidak" -> {
                kuesioner_group_2.check(R.id.tidak2)
            }
            "iya" ->{
                kuesioner_group_2.check(R.id.iya2)
            }
            else -> {
            }
        }
        when (itemString[2]) {
            "tidak" -> {
                kuesioner_group_3.check(R.id.tidak3)
            }
            "ragu" -> {
                kuesioner_group_3.check(R.id.ragu3)
            }
            "iya" -> {
                kuesioner_group_3.check(R.id.iya3)
            }
            else -> {
            }
        }
        when (itemString[3]) {
            "negatif" -> {
                kuesioner_group_4.check(R.id.tidak4)
            }
            "positif" -> {
                kuesioner_group_4.check(R.id.iya4)
            }
            "tidak tes" -> {
                kuesioner_group_4.check(R.id.tidaktes4)
            }
            else -> {
            }
        }
        // counting
        kuesioner_group_3.setOnCheckedChangeListener { _, checkedId ->
            itemString[2] = findViewById<RadioButton>(checkedId).text.toString().toLowerCase()
            Log.d("group 3", itemString.toString())
            calculate()
        }
        kuesioner_group_4.setOnCheckedChangeListener { _, checkedId ->
            itemString[3] = findViewById<RadioButton>(checkedId).text.toString().toLowerCase()
            Log.d("group 4", itemString.toString())
            calculate()
        }
    }
    private fun calculate(){
        likert = Likert(
            History(
                itemString[0],
                itemString[1],
                itemString[2],
                itemString[3],
                null,
                LocalDateTimeParser.nowOfLocal().toInstant().toString(),
                id,
                LocalDateTimeParser.nowOfLocal().toInstant().toString(),
                false,
            )
        ).get()
        Log.d("likert scale", likert.toString())
        validate()
    }
    private fun validate(){
        val enable = (itemString[2] != "null" && itemString[3] != "null")
        button_submit.apply {
            isClickable = enable
            isEnabled = enable
            setOnClickListener { submitVerification() }
        }
    }

    private fun stringToWords(s : String) = s.splitToSequence(',')
        .filter { it.isNotEmpty() } // or: .filter { it.isNotBlank() }
        .toList()

    private fun submitVerification() = launch {
        try {
            val data = DataPostQuestionnaire(itemString[2], itemString[3], likert.status, likert.accuracy.toInt())
            resultViewModel.submitVerification(id, data).observe(this@QuestionnaireDetailActivity, {
                val message = if (it){
                    "Berhasil memverifikasi status"
                } else "Gagal memverifikasi status"
                Toast.makeText(this@QuestionnaireDetailActivity, message, Toast.LENGTH_SHORT).show()
                if (it) finishAndRemoveTask()
            })
        } catch (e: Exception){
            Log.e("submitVerification", e.message, e)
            when(e) {
                is SocketTimeoutException -> {
                    Toast.makeText(this@QuestionnaireDetailActivity, "Tidak bisa terhubung dengan server", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this@QuestionnaireDetailActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}