package gsm.gsmnetindo.app_3s_checker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gsm.gsmnetindo.app_3s_checker.data.repository.BarcodeRepository

class BarcodeViewModelFactory(
    private val barcodeRepository: BarcodeRepository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BarcodeViewModel(barcodeRepository) as T
    }
}