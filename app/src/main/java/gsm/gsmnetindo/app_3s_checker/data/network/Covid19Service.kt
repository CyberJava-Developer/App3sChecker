package gsm.gsmnetindo.app_3s_checker.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import gsm.gsmnetindo.app_3s_checker.data.db.entity.CountryStatusItem
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface Covid19Service {

    @GET("country/indonesia")
    suspend fun dataAsync(): List<CountryStatusItem>

    companion object {
        operator fun invoke(
            networkRequestInterceptor: NetworkRequestInterceptor
        ): Covid19Service {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(networkRequestInterceptor)
                .build()
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.covid19api.com/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Covid19Service::class.java)
        }
    }

}