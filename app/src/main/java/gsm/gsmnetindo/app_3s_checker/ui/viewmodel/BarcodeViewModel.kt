package gsm.gsmnetindo.app_3s_checker.ui.viewmodel

import androidx.lifecycle.ViewModel
import gsm.gsmnetindo.app_3s_checker.data.repository.BarcodeRepository

class BarcodeViewModel(
    private val barcodeRepository: BarcodeRepository
): ViewModel() {
    suspend fun scan(code: String) = barcodeRepository.scanCode(code)
}