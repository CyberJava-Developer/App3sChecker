package gsm.gsmnetindo.app_3s_checker.ui.Intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gsm.gsmnetindo.app_3s_checker.data.repository.InstallManagerRepository

class IntroViewModelFactory(
    private val installManagerRepository: InstallManagerRepository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return IntroViewModel(installManagerRepository) as T
    }
}