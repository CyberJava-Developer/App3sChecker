package gsm.gsmnetindo.app_3s_checker.ui.splash

import androidx.lifecycle.ViewModel
import gsm.gsmnetindo.app_3s_checker.data.preference.installer.InstallManager
import gsm.gsmnetindo.app_3s_checker.data.repository.VersionRepository
import gsm.gsmnetindo.app_3s_checker.internal.network.Network

class SplashViewModel(
    private val network: Network,
    private val versionRepository: VersionRepository,
    private val installManager: InstallManager
): ViewModel() {
    fun isOnline() = network.isOnline()
    suspend fun getLatestVersion() = versionRepository.getVersion()
    fun isFirst() = installManager.isFirst()
}