package gsm.gsmnetindo.app_3s_checker.data.network

import androidx.lifecycle.LiveData
import gsm.gsmnetindo.app_3s_checker.data.network.response.UserLoginResponse
import gsm.gsmnetindo.app_3s_checker.data.network.response.VersionResponse
import gsm.gsmnetindo.app_3s_checker.data.network.response.barcode.BarcodeDetailResponse

interface RestApiNetworkDataSource {
    val downloadedVersion: LiveData<VersionResponse>
    val downloadedLoginResponse: LiveData<UserLoginResponse>
    val downloadedScanResponse: LiveData<BarcodeDetailResponse>
    suspend fun fetchVersion()
    suspend fun fetchLogin(phone: String)
    suspend fun fetchBarcode(code: String)
}