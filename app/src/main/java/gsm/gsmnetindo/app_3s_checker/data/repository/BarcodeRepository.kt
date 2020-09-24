package gsm.gsmnetindo.app_3s_checker.data.repository

import androidx.lifecycle.LiveData
import gsm.gsmnetindo.app_3s_checker.data.network.response.barcode.BarcodeDetailResponse
import gsm.gsmnetindo.app_3s_checker.data.network.response.observation.ObservationResponse

interface BarcodeRepository {
    suspend fun scanCode(code: String): LiveData<BarcodeDetailResponse>

    fun set(code: String, detailResponse: BarcodeDetailResponse)
    fun get(): LiveData<BarcodeDetailResponse>
    fun code(): LiveData<String>
    fun clear()

    suspend fun getObserved(): LiveData<ObservationResponse>
}