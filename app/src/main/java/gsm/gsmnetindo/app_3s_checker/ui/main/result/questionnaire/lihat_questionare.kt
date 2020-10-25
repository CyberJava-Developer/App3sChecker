package gsm.gsmnetindo.app_3s_checker.ui.main.result.questionnaire

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.internal.ScopedActivity
import gsm.gsmnetindo.app_3s_checker.ui.main.MainViewModel
import gsm.gsmnetindo.app_3s_checker.ui.main.MainViewModelFactory
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModel
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModelFactory
import kotlinx.android.synthetic.main.activity_lihat_questionare.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class lihat_questionare : ScopedActivity(), KodeinAware {

    override val kodein by closestKodein()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lihat_questionare)
        val question = intent.getStringExtra("questionare")
        val itemstring = stringToWords(question)
        Log.d("dari item", itemstring[0])
        when (itemstring[0]) {
            "tidak" -> {
                kuesioner_group_1.check(R.id.tidak1)
            }
            "iya" ->{
                kuesioner_group_1.check(R.id.iya1)
            }
            else -> {

            }
        }
        when (itemstring[1]) {
            "tidak" -> {
                kuesioner_group_2.check(R.id.tidak2)
            }
            "iya" ->{
                kuesioner_group_2.check(R.id.iya2)
            }
            else -> {
            }
        }
        when (itemstring[2]) {
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
        when (itemstring[3]) {
            "tidak" -> {
                kuesioner_group_4.check(R.id.tidak4)
            }
            "iya" -> {
                kuesioner_group_1.check(R.id.iya4)
            }
            else -> {
            }
        }
    }

    fun stringToWords(s : String) = s.splitToSequence(',')
        .filter { it.isNotEmpty() } // or: .filter { it.isNotBlank() }
        .toList()
}