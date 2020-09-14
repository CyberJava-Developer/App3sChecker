package gsm.gsmnetindo.app_3s_checker.data.preference.user

import androidx.lifecycle.LiveData

interface UserManager {
    fun login(phone: String, jwt: String, verified: Boolean, registered: Boolean)
    fun logout()
    fun isLoggedIn(): LiveData<Boolean>

    fun setPhone(phone: String)
    fun getPhone(): LiveData<String>
    fun getPhonePref(): String?

    fun setToken(jwt: String)
    fun getToken(): LiveData<String>
    fun getTokenPref(): String?

    fun setVerified(verified: Boolean)
    fun isVerified(): LiveData<Boolean>

    fun setRegistered(registered: Boolean)
    fun isRegistered(): LiveData<Boolean>

    fun setRole(role: Int)
    fun getRole(): LiveData<Int>
    fun getRolePref(): Int
}