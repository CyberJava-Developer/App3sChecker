package gsm.gsmnetindo.app_3s_checker.ui.dashboard.home

import androidx.lifecycle.ViewModel
import gsm.gsmnetindo.app_3s_checker.data.repository.Covid19Repository
import gsm.gsmnetindo.app_3s_checker.data.repository.FeedsRepository

class HomeViewModel(
    private val covid19Repository: Covid19Repository,
    private val feedsRepository: FeedsRepository
): ViewModel() {
    suspend fun covidData() = covid19Repository.getData()
    suspend fun feeds() = feedsRepository.getFeeds()
}