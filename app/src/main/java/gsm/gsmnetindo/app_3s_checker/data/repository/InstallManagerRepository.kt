package gsm.gsmnetindo.app_3s_checker.data.repository

import androidx.lifecycle.LiveData

interface InstallManagerRepository {
    fun setFirst()
    fun isFirst(): LiveData<Boolean>
}