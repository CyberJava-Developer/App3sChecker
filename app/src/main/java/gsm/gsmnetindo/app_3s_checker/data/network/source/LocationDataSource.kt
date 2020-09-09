package gsm.gsmnetindo.app_3s_checker.data.network.source

import android.location.Address
import android.location.Location
import androidx.lifecycle.LiveData

interface LocationDataSource {
    val location: LiveData<Location>
    val address: LiveData<Address>
    fun fetchLocation()
}