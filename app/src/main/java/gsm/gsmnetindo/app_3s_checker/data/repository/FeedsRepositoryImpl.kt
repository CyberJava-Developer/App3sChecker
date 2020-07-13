package gsm.gsmnetindo.app_3s_checker.data.repository

import androidx.lifecycle.LiveData
import gsm.gsmnetindo.app_3s_checker.data.db.FeedsDao
import gsm.gsmnetindo.app_3s_checker.data.db.entity.FeedItem
import gsm.gsmnetindo.app_3s_checker.data.network.RestApiNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FeedsRepositoryImpl(
    private val feedsDao: FeedsDao,
    private val restApiNetworkDataSource: RestApiNetworkDataSource
) : FeedsRepository {
    init {
        restApiNetworkDataSource.apply {
            downloadedFeedsResponse.observeForever {
                persistFeeds(it)
            }
        }
    }
    override suspend fun getFeeds(): LiveData<List<FeedItem>> {
        restApiNetworkDataSource.fetchFeeds()
        return withContext(Dispatchers.IO){
            return@withContext feedsDao.getFeeds()
        }
    }
    private fun persistFeeds(feeds: List<FeedItem>){
        fun deleteOldFeeds(){
            if (feedsDao.countFeeds() != 0) feedsDao.deleteFeeds()
        }
        GlobalScope.launch {
            deleteOldFeeds()
            feedsDao.upsert(feeds)
        }
    }
}