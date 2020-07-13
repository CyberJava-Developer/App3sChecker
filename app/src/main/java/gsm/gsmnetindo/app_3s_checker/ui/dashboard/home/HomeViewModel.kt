package gsm.gsmnetindo.app_3s_checker.ui.dashboard.home

import androidx.lifecycle.ViewModel
import gsm.gsmnetindo.app_3s_checker.data.repository.Covid19Repository

class HomeViewModel(
    private val covid19Repository: Covid19Repository
): ViewModel() {
    suspend fun covidData() = covid19Repository.getData()
}