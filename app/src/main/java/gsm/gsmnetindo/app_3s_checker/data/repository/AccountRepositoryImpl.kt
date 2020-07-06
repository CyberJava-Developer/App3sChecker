package gsm.gsmnetindo.app_3s_checker.data.repository

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import gsm.gsmnetindo.app_3s_checker.data.network.RestApiNetworkDataSource
import gsm.gsmnetindo.app_3s_checker.data.network.response.UserLoginResponse
import gsm.gsmnetindo.app_3s_checker.data.preference.user.UserManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class AccountRepositoryImpl(
    private val userManager: UserManager,
    private val restApiNetworkDataSource: RestApiNetworkDataSource
) : AccountRepository {
    override fun isLoggedIn() = userManager.isLoggedIn()

    override fun getLivePhone() = userManager.getPhone()

    override fun getPhonePref() = userManager.getPhonePref()

    override fun setPhone(phone: String) = userManager.setPhone(phone)
    override fun getToken() = userManager.getToken()

    override fun setVerified(verified: Boolean) {
        TODO("Not yet implemented")
    }

    override fun isVerified(): LiveData<Boolean> {
        TODO("Not yet implemented")
    }

    override fun setRegistered() = userManager.setRegistered(true)

    override fun isRegistered() = userManager.isRegistered()

    override fun doLogout() {
        GlobalScope.launch {
            FirebaseAuth.getInstance().signOut()
            userManager.logout()
        }
    }

    override suspend fun doLogin(phone: String): LiveData<UserLoginResponse> {
        restApiNetworkDataSource.fetchLogin(phone)
        return withContext(Dispatchers.IO){
            restApiNetworkDataSource.downloadedLoginResponse.value?.let {
                userManager.setPhone(it.phone)
                userManager.setToken(it.jwt)
            }
            return@withContext restApiNetworkDataSource.downloadedLoginResponse
        }
    }
}