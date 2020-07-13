package gsm.gsmnetindo.app_3s_checker.ui.dashboard.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gsm.gsmnetindo.app_3s_checker.data.repository.Covid19Repository

class HomeViewModelFactory(
    private val covid19Repository: Covid19Repository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(covid19Repository) as T
    }
}