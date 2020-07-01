package gsm.gsmnetindo.app_3s_checker.data.network

import androidx.lifecycle.LiveData
import gsm.gsmnetindo.app_3s_checker.data.network.response.VersionResponse

interface RestApiNetworkDataSource {
    val downloadedVersion: LiveData<VersionResponse>
    suspend fun fetchVersion()
}