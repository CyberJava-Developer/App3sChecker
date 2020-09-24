package gsm.gsmnetindo.app_3s_checker.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gsm.gsmnetindo.app_3s_checker.data.network.RestApiNetworkDataSource
import gsm.gsmnetindo.app_3s_checker.data.network.response.barcode.BarcodeDetailResponse
import gsm.gsmnetindo.app_3s_checker.data.network.response.observation.ObservationResponse
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
    private val _detail = MutableLiveData<BarcodeDetailResponse>()
    private val _code = MutableLiveData<String>()
    override fun get(): LiveData<BarcodeDetailResponse> = _detail
    override fun code(): LiveData<String> = _code
    override fun set(code: String, detailResponse: BarcodeDetailResponse) {
        _detail.postValue(detailResponse)
        _code.postValue(code)
    }

    override fun clear() {
        _code.postValue(null)
        _detail.postValue(null)
    }
    override suspend fun getObserved(): LiveData<ObservationResponse> {
        restApiNetworkDataSource.fetchLocation()
        return withContext(Dispatchers.IO){
            return@withContext restApiNetworkDataSource.downloadedLocationsResponse
        }
    }
}