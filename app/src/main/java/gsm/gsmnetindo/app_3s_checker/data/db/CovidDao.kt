package gsm.gsmnetindo.app_3s_checker.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gsm.gsmnetindo.app_3s_checker.data.db.entity.CovidDataItem

@Dao
interface CovidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(items: List<CovidDataItem>)

    @Query("select * from days_status")
    fun getAll(): LiveData<List<CovidDataItem>>

    @Query("select count(id) from days_status")
    fun count(): LiveData<Int>

    @Query("delete from days_status")
    fun deleteAll()
}