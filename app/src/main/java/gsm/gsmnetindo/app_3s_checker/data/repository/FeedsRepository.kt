package gsm.gsmnetindo.app_3s_checker.data.repository

import androidx.lifecycle.LiveData
import gsm.gsmnetindo.app_3s_checker.data.db.entity.FeedItem

interface FeedsRepository {
    suspend fun getFeeds(): LiveData<List<FeedItem>>
}