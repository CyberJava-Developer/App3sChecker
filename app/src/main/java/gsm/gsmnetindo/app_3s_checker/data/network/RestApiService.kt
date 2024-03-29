package gsm.gsmnetindo.app_3s_checker.data.network

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import gsm.gsmnetindo.app_3s_checker.data.db.entity.FeedItem
import gsm.gsmnetindo.app_3s_checker.data.network.body.DataPostLogin
import gsm.gsmnetindo.app_3s_checker.data.network.body.DataPostQuestionnaire
import gsm.gsmnetindo.app_3s_checker.data.network.response.UserLoginResponse
import gsm.gsmnetindo.app_3s_checker.data.network.response.VersionResponse
import gsm.gsmnetindo.app_3s_checker.data.network.response.barcode.BarcodeDetailResponse
import gsm.gsmnetindo.app_3s_checker.data.network.response.detail.UserDetailResponse
import gsm.gsmnetindo.app_3s_checker.data.network.response.observation.ObservationResponse
import gsm.gsmnetindo.app_3s_checker.internal.Secret
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface RestApiService {

    @GET("version")
    suspend fun versionAsync(): VersionResponse

    @POST("login")
    suspend fun userLoginAsync(
        @Body dataPostLogin: DataPostLogin
    ): UserLoginResponse

    @GET("{phone}/feeds.json")
    suspend fun getFeedsAsync(
        @Path("phone") phone: String
    ) : List<FeedItem>

    @GET("{phone}/detail.json")
    suspend fun fetchDetailAsync(
        @Path("phone") phone: String
    ): UserDetailResponse

    @GET("{phone}/scan/{code}")
    suspend fun scanCodeAsync(
        @Path("phone") phone: String,
        @Path("code") code: String
    ): BarcodeDetailResponse

    @GET("{phone}/locations.json")
    suspend fun observeAsync(
        @Path("phone") phone: String
    ): ObservationResponse

    @POST("{phone}/questionnaire.json/{id}")
    suspend fun verifyAsync(
        @Path("phone") phone: String,
        @Path("id") questId: Int,
        @Body dataPostQuestionnaire: DataPostQuestionnaire
    ): Boolean

    companion object {
        operator fun invoke(
            networkRequestInterceptor: NetworkRequestInterceptor
        ): RestApiService {

            val requestInterceptor =  Interceptor {chain ->
                val url = chain.request()
                    .url()
                val request = chain.request()
                    .newBuilder()
                    .addHeader("Api-Key", Secret.apiKey())
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request).also {
                    Log.i("RestApiService", it.toString())
                }
            }
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(requestInterceptor)
                .addInterceptor(networkRequestInterceptor)
                .build()
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("${Secret.baseApi()}${Secret.apiVersion()}/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RestApiService::class.java)
        }
    }

}