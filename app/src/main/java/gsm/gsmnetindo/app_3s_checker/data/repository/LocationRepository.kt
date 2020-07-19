package gsm.gsmnetindo.app_3s_checker.data.repository

import android.location.Address
import android.location.Location
import androidx.lifecycle.LiveData

interface LocationRepository {
    suspend fun getLocation(): LiveData<Location>
    suspend fun getAddress(): LiveData<Address>
}