package gsm.gsmnetindo.app_3s_checker.data.repository

import android.location.Address
import android.location.Location
import androidx.lifecycle.LiveData
import gsm.gsmnetindo.app_3s_checker.data.network.LocationDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationRepositoryImpl(
    private val locationDataSource: LocationDataSource
) : LocationRepository {
    init {

    }
    override suspend fun getLocation(): LiveData<Location> {
        locationDataSource.fetchLocation()
        return withContext(Dispatchers.IO){
            return@withContext locationDataSource.location
        }
    }

    override suspend fun getAddress(): LiveData<Address> {
        locationDataSource.fetchLocation()
        return withContext(Dispatchers.IO){
            return@withContext locationDataSource.address
        }
    }
}