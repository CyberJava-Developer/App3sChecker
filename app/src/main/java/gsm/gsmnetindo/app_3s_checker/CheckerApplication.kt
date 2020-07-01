package gsm.gsmnetindo.app_3s_checker

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import gsm.gsmnetindo.app_3s_checker.data.network.*
import gsm.gsmnetindo.app_3s_checker.data.preference.installer.InstallManager
import gsm.gsmnetindo.app_3s_checker.data.preference.installer.InstallManagerImpl
import gsm.gsmnetindo.app_3s_checker.data.repository.InstallManagerRepository
import gsm.gsmnetindo.app_3s_checker.data.repository.InstallManagerRepositoryImpl
import gsm.gsmnetindo.app_3s_checker.data.repository.VersionRepository
import gsm.gsmnetindo.app_3s_checker.data.repository.VersionRepositoryImpl
import gsm.gsmnetindo.app_3s_checker.internal.network.Network
import gsm.gsmnetindo.app_3s_checker.internal.network.NetworkImpl
import gsm.gsmnetindo.app_3s_checker.ui.Intro.IntroViewModelFactory
import gsm.gsmnetindo.app_3s_checker.ui.splash.SplashViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class CheckerApplication: Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@CheckerApplication))
        // Dependency Injection here

        // network state
        bind<Network>() with singleton { NetworkImpl(instance()) }

        // install manager
        bind<InstallManager>() with singleton { InstallManagerImpl(instance()) }
        bind<InstallManagerRepository>() with singleton { InstallManagerRepositoryImpl(instance()) }

        // networking
        bind<NetworkRequestInterceptor>() with singleton { NetworkRequestInterceptorImpl(instance()) }
        bind() from singleton { RestApiService(instance()) }
        bind<RestApiNetworkDataSource>() with singleton { RestApiNetworkDataSourceImpl(instance()) }

        // repository
        bind<VersionRepository>() with singleton { VersionRepositoryImpl(instance()) }

        // view model factory
        bind() from  provider { SplashViewModelFactory(instance(), instance(), instance()) }
        bind() from provider { IntroViewModelFactory(instance()) }

    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }

}