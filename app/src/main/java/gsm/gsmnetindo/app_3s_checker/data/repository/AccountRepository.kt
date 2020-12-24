package gsm.gsmnetindo.app_3s_checker.data.repository

import androidx.lifecycle.LiveData
import gsm.gsmnetindo.app_3s_checker.data.network.response.UserLoginResponse
import gsm.gsmnetindo.app_3s_checker.data.network.response.detail.UserDetailResponse
import okhttp3.MultipartBody

interface AccountRepository {
    fun isLoggedIn(): LiveData<Boolean>

    fun getLivePhone(): LiveData<String>
    fun getPhonePref(): String?
    fun setPhone(phone: String)
    fun setToken(token: String)
    fun getToken(): LiveData<String>

    fun setVerified(verified: Boolean)
    fun isVerified(): LiveData<Boolean>
    fun setRegistered(registered: Boolean)
    fun setRegistered()
    fun isRegistered(): LiveData<Boolean>

    fun setToken(token: String)

    fun doLogout()

    suspend fun doLogin(phone: String): LiveData<UserLoginResponse>
    fun setRole(role: Int)
    fun getRolePref(): Int

    suspend fun fetchDetail()
    val detail: LiveData<UserDetailResponse>
}