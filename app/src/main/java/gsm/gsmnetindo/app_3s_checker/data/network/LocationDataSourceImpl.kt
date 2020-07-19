package gsm.gsmnetindo.app_3s_checker.data.network

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import gsm.gsmnetindo.app_3s_checker.internal.LocationNotEnabledException
import gsm.gsmnetindo.app_3s_checker.internal.LocationPermissionException
import java.util.*

class LocationDataSourceImpl(
    private val application: Application,
    private val locationManager: LocationManager
) : LocationDataSource {

    private var fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application.applicationContext)
    private lateinit var mLocationRequest: LocationRequest
    private var geoCoder: Geocoder = Geocoder(application.applicationContext, Locale.getDefault())

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            if (p0 != null) {
                _location.postValue(p0.lastLocation)
                _address.postValue(addressLocator(p0.lastLocation))
            }
        }

        override fun onLocationAvailability(p0: LocationAvailability?) {
            if (p0 != null) {
                if (p0.isLocationAvailable) requestNewLocation()
            }
        }
    }

    private val _location = MutableLiveData<Location>()
    override val location: LiveData<Location>
        get() = _location
    private val _address = MutableLiveData<Address>()
    override val address: LiveData<Address>
        get() = _address
    @SuppressLint("MissingPermission")
    override fun fetchLocation() {
        if (isPermissionGranted()){
            if (isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                    if (it.isSuccessful) {
                        _location.postValue(it.result)
                        _address.postValue(addressLocator(it.result!!))
                    }
                    else requestNewLocation()
                }
                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    if (it != null){
                        _location.postValue(it)
                        _address.postValue(addressLocator(it))
                    }
                    else requestNewLocation()
                }
            } else {
                throw LocationNotEnabledException()
            }
        } else {
            throw LocationPermissionException()
        }

    }
    private fun addressLocator(location: Location): Address{
        return geoCoder.getFromLocation(location.latitude, location.longitude, 1)[0]
    }
    @SuppressLint("MissingPermission")
    private fun requestNewLocation(){
        mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5
        mLocationRequest.fastestInterval = 3
        mLocationRequest.numUpdates = 1

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application)
        fusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
        if (isPermissionGranted()) throw LocationPermissionException()
        if (!isLocationEnabled()) throw LocationNotEnabledException()
        fusedLocationProviderClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback, Looper.getMainLooper()
        )
    }
    private fun isPermissionGranted(): Boolean{
        return ActivityCompat.checkSelfPermission(
            application.applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            application.applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun isLocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}