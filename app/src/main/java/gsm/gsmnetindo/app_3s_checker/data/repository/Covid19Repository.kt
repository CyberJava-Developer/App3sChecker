package gsm.gsmnetindo.app_3s_checker.data.repository

import androidx.lifecycle.LiveData
import gsm.gsmnetindo.app_3s_checker.data.db.entity.CovidDataItem

interface Covid19Repository {
    suspend fun getCount(): LiveData<Int>
    suspend fun getData(): LiveData<List<CovidDataItem>>
}