package gsm.gsmnetindo.app_3s_checker.data.repository

import androidx.lifecycle.LiveData
import gsm.gsmnetindo.app_3s_checker.data.network.RestApiNetworkDataSource
import gsm.gsmnetindo.app_3s_checker.data.network.response.barcode.BarcodeDetailResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BarcodeRepositoryImpl(
    private val restApiNetworkDataSource: RestApiNetworkDataSource
) : BarcodeRepository {
    override suspend fun scanCode(code: String): LiveData<BarcodeDetailResponse> {
        restApiNetworkDataSource.fetchBarcode(code)
        return withContext(Dispatchers.IO){
            return@withContext restApiNetworkDataSource.downloadedScanResponse
        }
    }
}