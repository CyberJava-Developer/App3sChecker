package gsm.gsmnetindo.app_3s_checker.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import gsm.gsmnetindo.app_3s_checker.data.network.RestApiNetworkDataSource
import gsm.gsmnetindo.app_3s_checker.data.network.response.UserLoginResponse
import gsm.gsmnetindo.app_3s_checker.data.network.response.detail.UserDetailResponse
import gsm.gsmnetindo.app_3s_checker.data.preference.user.UserManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.lang.Exception

class AccountRepositoryImpl(
    private val userManager: UserManager,
    private val restApiNetworkDataSource: RestApiNetworkDataSource
) : AccountRepository {
    init {
        restApiNetworkDataSource.downloadedDetailResponse.observeForever {
            if (it == null) {
                GlobalScope.launch {
//                    restApiNetworkDataSource.fetchDetail()
                }
            } else {
                _detail.postValue(it)
            }
        }
        userManager.isLoggedIn().observeForever{
            if (it){
                GlobalScope.launch {
//                    restApiNetworkDataSource.fetchDetail()
                }
            }
        }
    }

    override fun isLoggedIn() = userManager.isLoggedIn()

    override fun getLivePhone() = userManager.getPhone()

    override fun getPhonePref() = userManager.getPhonePref()

    override fun setPhone(phone: String) = userManager.setPhone(phone)
    override fun setToken(token: String) = userManager.setToken(token)

    override fun getToken() = userManager.getToken()

    override fun setVerified(verified: Boolean) = userManager.setVerified(verified)
    override fun isVerified() = userManager.isVerified()

    override fun setRegistered(registered: Boolean) = userManager.setRegistered(registered)

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
        return withContext(Dispatchers.IO) {
            restApiNetworkDataSource.downloadedLoginResponse.value?.let {
//                userManager.setPhone(it.phone)
//                userManager.setToken(it.jwt)
//                userManager.setRole(it.role)
            }
            return@withContext restApiNetworkDataSource.downloadedLoginResponse
        }
    }

    override fun setRole(role: Int) {
        userManager.setRole(role)
    }

    override fun getRolePref() = userManager.getRolePref()

    private val _detail = MutableLiveData<UserDetailResponse>()
    override val detail: LiveData<UserDetailResponse>
        get() = _detail

    override suspend fun fetchDetail() {
        try {
            restApiNetworkDataSource.fetchDetail()
        } catch (e: Exception){
            throw e
        }
    }
}