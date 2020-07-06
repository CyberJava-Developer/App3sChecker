package gsm.gsmnetindo.app_3s_checker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gsm.gsmnetindo.app_3s_checker.data.repository.AccountRepository

class AccountViewModelFactory(
    private val accountRepository: AccountRepository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AccountViewModel(accountRepository) as T
    }
}