package gsm.gsmnetindo.app_3s_checker.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gsm.gsmnetindo.app_3s_checker.data.db.entity.FeedItem

@Dao
interface FeedsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(feedItems: List<FeedItem>)

    @Query("select * from feeds")
    fun getFeeds(): LiveData<List<FeedItem>>

    @Query("select count(id) from feeds")
    fun countFeeds(): Int

    @Query("delete from feeds")
    fun deleteFeeds()
}