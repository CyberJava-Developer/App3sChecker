package gsm.gsmnetindo.app_3s_checker.data.repository

import androidx.lifecycle.LiveData
import gsm.gsmnetindo.app_3s_checker.data.db.CovidDao
import gsm.gsmnetindo.app_3s_checker.data.db.entity.CountryStatusItem
import gsm.gsmnetindo.app_3s_checker.data.network.RestApiNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Covid19RepositoryImpl(
    private val covidDao: CovidDao,
    private val restApiNetworkDataSource: RestApiNetworkDataSource
) : Covid19Repository {
    init {
        restApiNetworkDataSource.apply {
            dataCovid19Service.observeForever{
                persistData(it)
            }
        }
    }
    override suspend fun getCount(): LiveData<Int> {
        return withContext(Dispatchers.IO){
            return@withContext covidDao.count()
        }
    }

    override suspend fun getData(): LiveData<List<CountryStatusItem>> {
        restApiNetworkDataSource.fetchCovidData()
        return withContext(Dispatchers.IO){
            return@withContext covidDao.getAll()
        }
    }
    private fun persistData(data: List<CountryStatusItem>){
        fun deleteOldData(){
            covidDao.deleteAll()
        }
        GlobalScope.launch {
            deleteOldData()
            covidDao.upsert(data)
        }
    }
}