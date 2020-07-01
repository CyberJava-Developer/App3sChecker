package gsm.gsmnetindo.app_3s_checker.ui.Intro

import androidx.lifecycle.ViewModel
import gsm.gsmnetindo.app_3s_checker.data.repository.InstallManagerRepository

class IntroViewModel(
    private val installManagerRepository: InstallManagerRepository
): ViewModel() {
    fun setFirst() = installManagerRepository.setFirst()

    fun isFirst() = installManagerRepository.isFirst()
}