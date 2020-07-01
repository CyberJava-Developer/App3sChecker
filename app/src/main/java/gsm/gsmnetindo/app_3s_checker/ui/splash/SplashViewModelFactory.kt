package gsm.gsmnetindo.app_3s_checker.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gsm.gsmnetindo.app_3s_checker.data.preference.installer.InstallManager
import gsm.gsmnetindo.app_3s_checker.data.repository.VersionRepository
import gsm.gsmnetindo.app_3s_checker.internal.network.Network

class SplashViewModelFactory(
    private val network: Network,
    private val versionRepository: VersionRepository,
    private val installManager: InstallManager
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel(network, versionRepository, installManager) as T
    }
}