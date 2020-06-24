package gsm.gsmnetindo.app_3s_checker

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class CheckerApplication: Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        // Dependency Injection here

    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }

}