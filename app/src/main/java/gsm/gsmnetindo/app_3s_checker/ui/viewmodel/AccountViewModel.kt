package gsm.gsmnetindo.app_3s_checker.ui.viewmodel

import androidx.lifecycle.ViewModel
import gsm.gsmnetindo.app_3s_checker.data.repository.AccountRepository

class AccountViewModel(
    private val accountRepository: AccountRepository
) : ViewModel() {
    fun isLoggedIn() = accountRepository.isLoggedIn()
    suspend fun login(phone: String) = accountRepository.doLogin(phone)
    fun logout() = accountRepository.doLogout()

    val detail = accountRepository.detail
    suspend fun syncDetail() = accountRepository.fetchDetail()


    fun getPhone() = accountRepository.getLivePhone()
    fun getPhonePref() = accountRepository.getPhonePref()

    fun isRegistered() = accountRepository.isRegistered()
    fun setRegistered() = accountRepository.setRegistered()

    fun getToken() = accountRepository.getToken()

    fun setRole(role: Int) = accountRepository.setRole(role)
    fun getRolePref() = accountRepository.getRolePref()
}