package gsm.gsmnetindo.app_3s_checker.data.preference.installer

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class InstallManagerImpl(
    context: Context
) : InstallManager {
    companion object{
        private const val PRIVATE_MODE = 0
        private const val PREFERENCE_NAME = "configuration"
        private const val FIRST_INSTALL = "isFirstInstall"
    }
    private val preferences: SharedPreferences
    private val editor: SharedPreferences.Editor
    private val first = MutableLiveData<Boolean>()
    init {
        preferences = context.getSharedPreferences(
            PREFERENCE_NAME,
            PRIVATE_MODE
        )
        editor = preferences.edit()
        first.apply {
            value = preferences.getBoolean(FIRST_INSTALL, true)
        }
    }

    override fun setFirst() {
        editor.putBoolean(FIRST_INSTALL, false).commit()
        editor.commit()
        first.apply {
            value = preferences.getBoolean(FIRST_INSTALL, true)
        }
        Log.i("setFirst", "${first.value}")
    }

    override fun isFirst() : LiveData<Boolean> {
        Log.i("isFirst", "${first.value}")
        return first
    }
}