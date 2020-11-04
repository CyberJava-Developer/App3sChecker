package gsm.gsmnetindo.app_3s_checker.data.network

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gsm.gsmnetindo.app_3s_checker.data.db.entity.CovidDataItem
import gsm.gsmnetindo.app_3s_checker.data.db.entity.FeedItem
import gsm.gsmnetindo.app_3s_checker.data.network.body.DataPostLogin
import gsm.gsmnetindo.app_3s_checker.data.network.body.DataPostQuestionnaire
import gsm.gsmnetindo.app_3s_checker.data.network.response.UserLoginResponse
import gsm.gsmnetindo.app_3s_checker.data.network.response.VersionResponse
import gsm.gsmnetindo.app_3s_checker.data.network.response.barcode.BarcodeDetailResponse
import gsm.gsmnetindo.app_3s_checker.data.network.response.detail.UserDetailResponse
import gsm.gsmnetindo.app_3s_checker.data.network.response.observation.ObservationResponse
import retrofit2.HttpException

class RestApiNetworkDataSourceImpl(
    private val context: Context,
    private val covid19Service: Covid19Service,
    private val restApiService: RestApiService
) : RestApiNetworkDataSource {
    fun getPhone(): String {
        val con = context.applicationContext
        val PRIVATE_MODE = 0
        val PREFERENCE_NAME_PHONE = "phone"
        val ACCOUNT_PHONE = "phone"
        val phonePreferences: SharedPreferences
        val phoneEditor: SharedPreferences.Editor
        phonePreferences = con.getSharedPreferences(
            PREFERENCE_NAME_PHONE,
            PRIVATE_MODE
        )
        val phone = phonePreferences.getString(ACCOUNT_PHONE, "")
        Log.d("phone is", "$phone")
        return phonePreferences.getString(ACCOUNT_PHONE, "")!!
    }

    // for getting covid-19 data
    private val _dataCovid = MutableLiveData<List<CovidDataItem>>()
    override val dataCovid19Service: LiveData<List<CovidDataItem>>
        get() = _dataCovid

    override suspend fun fetchCovidData() {
        try {
            covid19Service.dataAsync().apply {
                _dataCovid.postValue(this)
            }
        } catch (e: HttpException) {
            throw e
        }
    }

    // for version
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

    // for login
    private val _downloadedJWT = MutableLiveData<UserLoginResponse>()
    override val downloadedLoginResponse: LiveData<UserLoginResponse>
        get() = _downloadedJWT
    override suspend fun fetchLogin(phone: String) {
        val data = DataPostLogin(phone)
        Log.d("fetchJWTLogin", data.toString())
        try {
            restApiService
                .userLoginAsync(data).apply {
                    _downloadedJWT.postValue(this)
                }
        } catch (e: HttpException) {
            //Toast.makeText(context, e.message().toString(), Toast.LENGTH_SHORT).show()
            Log.d("Login Attempt", e.message.toString())
            throw e
        }
    }

    // for feeds
    private val _downloadedFeedsResponse = MutableLiveData<List<FeedItem>>()
    override val downloadedFeedsResponse: LiveData<List<FeedItem>>
        get() = _downloadedFeedsResponse

    override suspend fun fetchFeeds() {
        try {
            restApiService.getFeedsAsync(getPhone()).apply {
                _downloadedFeedsResponse.postValue(this)
            }
        } catch (e: HttpException){
            throw e
        }
    }

    // for user detail
    private val _downloadedDetailResponse = MutableLiveData<UserDetailResponse>()
    override val downloadedDetailResponse: LiveData<UserDetailResponse>
        get() = _downloadedDetailResponse
    override suspend fun fetchDetail() {
        try {
            restApiService.fetchDetailAsync(getPhone()).apply {
                _downloadedDetailResponse.postValue(this)
            }
        } catch (e: Exception) {
            Log.e("detch detail ds", e.message.toString(), e)
            throw e
        }
    }

    // for barcode
    private val _downloadedBarcodeResponse = MutableLiveData<BarcodeDetailResponse>()
    override val downloadedScanResponse: LiveData<BarcodeDetailResponse>
        get() = _downloadedBarcodeResponse
    override suspend fun fetchBarcode(code: String) {
        try {
            restApiService.scanCodeAsync(getPhone(), code).apply {
                _downloadedBarcodeResponse.postValue(this)
            }
        } catch (e: HttpException) {
            Log.e("scan Attempt", e.message.toString(), e)
            throw e
        }
    }

    // for observer
    private val _downloadedLocationsResponse = MutableLiveData<ObservationResponse>()
    override val downloadedLocationsResponse: LiveData<ObservationResponse>
        get() = _downloadedLocationsResponse
    override suspend fun fetchLocation() {
        try {
            restApiService.observeAsync(getPhone()).apply {
                _downloadedLocationsResponse.postValue(this)
            }
        } catch (e: HttpException) {
            Log.e("location all Attempt", e.message.toString(), e)
            throw e
        }
    }

    private val _downloadedVerifyResponse = MutableLiveData<Boolean>()
    override val downloadedVerifyResponse: LiveData<Boolean>
        get() = _downloadedVerifyResponse

    override suspend fun fetchVerify(id: Int, dataPostQuestionnaire: DataPostQuestionnaire) {
        try {
            restApiService.verifyAsync(getPhone(), id, dataPostQuestionnaire).apply {
                _downloadedVerifyResponse.postValue(this)
            }
        } catch (e: Exception){
            Log.e("verify questionnaire", e.message.toString(), e)
            throw e
        }
    }
}