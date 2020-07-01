package gsm.gsmnetindo.app_3s_checker.data.repository

import androidx.lifecycle.LiveData
import gsm.gsmnetindo.app_3s_checker.data.network.RestApiNetworkDataSource
import gsm.gsmnetindo.app_3s_checker.data.network.response.VersionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VersionRepositoryImpl(
    private val restApiNetworkDataSource: RestApiNetworkDataSource
) : VersionRepository {
    override suspend fun getVersion(): LiveData<VersionResponse> {
        restApiNetworkDataSource.fetchVersion()
        return withContext(Dispatchers.IO){
            return@withContext restApiNetworkDataSource.downloadedVersion
        }
    }
}