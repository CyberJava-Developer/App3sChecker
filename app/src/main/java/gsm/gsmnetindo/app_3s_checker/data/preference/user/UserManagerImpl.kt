package gsm.gsmnetindo.app_3s_checker.data.preference.user

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class UserManagerImpl(
    context: Context
) : UserManager {
    companion object{
        private const val PRIVATE_MODE = 0

        private const val PREFERENCE_NAME_PHONE = "phone"
        private const val PREFERENCE_NAME_JWT = "jwt"
        private const val PREFERENCE_NAME_VERIFIED = "verified"
        private const val PREFERENCE_NAME_REGISTERED = "registered"

        private const val ACCOUNT_PHONE = "phone"
        private const val ACCOUNT_JWT = "jwt"
        private const val ACCOUNT_VERIFIED = "verified"
        private const val ACCOUNT_REGISTERED = "registered"
    }

    private val phonePreferences: SharedPreferences
    private val jwtPreferences: SharedPreferences
    private val verifiedPreferences: SharedPreferences
    private val registeredPreferences: SharedPreferences

    private val phoneEditor: SharedPreferences.Editor
    private val jwtEditor: SharedPreferences.Editor
    private val verifiedEditor: SharedPreferences.Editor
    private val registeredEditor: SharedPreferences.Editor

    private val phone = MutableLiveData<String>()
    private val jwt = MutableLiveData<String>()
    private val verified = MutableLiveData<Boolean>()
    private val registered = MutableLiveData<Boolean>()
    init {
        phonePreferences = context.getSharedPreferences(
            PREFERENCE_NAME_PHONE,
            PRIVATE_MODE
        )
        jwtPreferences = context.getSharedPreferences(
            PREFERENCE_NAME_JWT,
            PRIVATE_MODE
        )
        verifiedPreferences = context.getSharedPreferences(
            PREFERENCE_NAME_VERIFIED,
            PRIVATE_MODE
        )
        registeredPreferences = context.getSharedPreferences(
            PREFERENCE_NAME_REGISTERED,
            PRIVATE_MODE
        )
        phoneEditor = phonePreferences.edit()
        jwtEditor = jwtPreferences.edit()
        verifiedEditor = verifiedPreferences.edit()
        registeredEditor = registeredPreferences.edit()

        phone.postValue(phonePreferences.getString(ACCOUNT_PHONE, ""))
        jwt.postValue(jwtPreferences.getString(ACCOUNT_JWT, ""))
        verified.postValue(verifiedPreferences.getBoolean(ACCOUNT_VERIFIED, false))
        registered.postValue(registeredPreferences.getBoolean(ACCOUNT_REGISTERED, false))
    }
    private fun reInit(){
        phone.postValue(phonePreferences.getString(ACCOUNT_PHONE, ""))
        jwt.postValue(jwtPreferences.getString(ACCOUNT_JWT, ""))
        verified.postValue(verifiedPreferences.getBoolean(ACCOUNT_VERIFIED, false))
        registered.postValue(registeredPreferences.getBoolean(ACCOUNT_REGISTERED, false))
    }
    override fun login(phone: String, jwt: String, verified: Boolean, registered: Boolean) {
        setPhone(phone)
        setToken(jwt)
        setRegistered(registered)
        reInit()
    }

    override fun logout() {
        setPhone("")
        setToken("")
        setRegistered(false)
        reInit()
    }

    override fun isLoggedIn(): LiveData<Boolean> {
        val p = phonePreferences.getString(ACCOUNT_PHONE, "") == ""
        val j = jwtPreferences.getString(ACCOUNT_JWT, "") == ""
        val pj = MutableLiveData<Boolean>().apply {
            value = phonePreferences.getString(ACCOUNT_PHONE, "") != ""
                    && jwtPreferences.getString(ACCOUNT_JWT, "") != ""
        }
        //pj.postValue(p && j)
        Log.d("isLoginDaoData", "${phone.value} & ${jwt.value}")
        Log.d("isLoginDao", "$p & $j = ${pj.value}")
        return pj
    }

    override fun setPhone(phone: String) {
        Log.i("setPhone","-{$phone}-")
        phoneEditor.putString(PREFERENCE_NAME_PHONE, phone).commit()
        phoneEditor.commit()
        reInit()
    }

    override fun getPhone() = phone as LiveData<String>

    override fun getPhonePref() = phonePreferences.getString(ACCOUNT_PHONE, "")

    override fun setToken(jwt: String) {
        Log.i("setJWT","-{$jwt}-")
        jwtEditor.putString(PREFERENCE_NAME_JWT, jwt).commit()
        jwtEditor.commit()
        reInit()
    }

    override fun getToken() = jwt as LiveData<String>

    override fun getTokenPref() = jwtPreferences.getString(ACCOUNT_JWT, "")

    override fun setVerified(verified: Boolean) {
        Log.i("setVerified", "-{$verified}-")
        verifiedEditor.putBoolean(PREFERENCE_NAME_VERIFIED, verified).commit()
        verifiedEditor.commit()
        reInit()
    }

    override fun isVerified() : LiveData<Boolean> = verified

    override fun setRegistered(registered: Boolean) {
        Log.i("setRegistered", "-{$registered}-")
        registeredEditor.putBoolean(PREFERENCE_NAME_REGISTERED, registered).commit()
        registeredEditor.commit()
        reInit()
    }

    override fun isRegistered() = registered as LiveData<Boolean>
}