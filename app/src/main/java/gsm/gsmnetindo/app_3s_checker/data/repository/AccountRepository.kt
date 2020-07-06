package gsm.gsmnetindo.app_3s_checker.data.repository

import androidx.lifecycle.LiveData
import gsm.gsmnetindo.app_3s_checker.data.network.response.UserLoginResponse
import okhttp3.MultipartBody

interface AccountRepository {
    fun isLoggedIn(): LiveData<Boolean>

    fun getLivePhone(): LiveData<String>
    fun getPhonePref(): String?
    fun setPhone(phone: String)

    fun getToken(): LiveData<String>

    fun setVerified(verified: Boolean)
    fun isVerified(): LiveData<Boolean>

    fun setRegistered()
    fun isRegistered(): LiveData<Boolean>

    fun doLogout()

    suspend fun doLogin(phone: String): LiveData<UserLoginResponse>
}