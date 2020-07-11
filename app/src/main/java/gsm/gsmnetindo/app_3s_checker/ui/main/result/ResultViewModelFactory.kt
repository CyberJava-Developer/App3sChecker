package gsm.gsmnetindo.app_3s_checker.ui.main.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gsm.gsmnetindo.app_3s_checker.data.repository.BarcodeRepository

class ResultViewModelFactory(
    private val barcodeRepository: BarcodeRepository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ResultViewModel(barcodeRepository) as T
    }
}