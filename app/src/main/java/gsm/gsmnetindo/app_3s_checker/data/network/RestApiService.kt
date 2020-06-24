package gsm.gsmnetindo.app_3s_checker.data.network

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import gsm.gsmnetindo.app_3s_checker.data.network.response.VersionResponse
import gsm.gsmnetindo.app_3s_checker.internal.Secret
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface RestApiService {

    @GET("version")
    suspend fun versionAsync(): VersionResponse

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