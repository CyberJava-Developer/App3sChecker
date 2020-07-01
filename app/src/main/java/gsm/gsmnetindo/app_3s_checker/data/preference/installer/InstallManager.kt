package gsm.gsmnetindo.app_3s_checker.data.preference.installer

import androidx.lifecycle.LiveData

interface InstallManager {
    fun setFirst()
    fun isFirst(): LiveData<Boolean>
}