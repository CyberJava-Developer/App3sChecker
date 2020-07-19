package gsm.gsmnetindo.app_3s_checker.ui.main

import androidx.lifecycle.ViewModel
import gsm.gsmnetindo.app_3s_checker.data.repository.LocationRepository

class MainViewModel(
    private val locationRepository: LocationRepository
): ViewModel() {
    suspend fun getLocation() = locationRepository.getLocation()
    suspend fun getAddress() = locationRepository.getAddress()
}