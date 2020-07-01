package gsm.gsmnetindo.app_3s_checker.data.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gsm.gsmnetindo.app_3s_checker.data.network.response.VersionResponse
import retrofit2.HttpException

class RestApiNetworkDataSourceImpl(
    private val restApiService: RestApiService
) : RestApiNetworkDataSource {
    private val _downloadedVersion = MutableLiveData<VersionResponse>()
    override val downloadedVersion: LiveData<VersionResponse>
        get() = _downloadedVersion

    override suspend fun fetchVersion() {
        try {
            restApiService.versionAsync().apply {
                _downloadedVersion.postValue(this)
            }
        } catch (e: HttpException) {
            throw e
        }
    }
}