package gsm.gsmnetindo.app_3s_checker.data.repository

import androidx.lifecycle.LiveData
import gsm.gsmnetindo.app_3s_checker.data.preference.installer.InstallManager

class InstallManagerRepositoryImpl(
    private val installManager: InstallManager
) : InstallManagerRepository {
    override fun setFirst() {
        installManager.setFirst()
    }

    override fun isFirst() = installManager.isFirst()
}