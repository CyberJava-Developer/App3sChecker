package gsm.gsmnetindo.app_3s_checker.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gsm.gsmnetindo.app_3s_checker.data.db.entity.CountryStatusItem

@Dao
interface CovidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(items: List<CountryStatusItem>)

    @Query("select * from days_status")
    fun getAll(): LiveData<List<CountryStatusItem>>

    @Query("select count(id) from days_status")
    fun count(): LiveData<Int>

    @Query("delete from days_status")
    fun deleteAll()
}