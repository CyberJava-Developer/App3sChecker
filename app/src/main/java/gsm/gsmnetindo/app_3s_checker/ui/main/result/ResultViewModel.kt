package gsm.gsmnetindo.app_3s_checker.ui.main.result

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.data.network.response.barcode.BarcodeDetailResponse
import gsm.gsmnetindo.app_3s_checker.data.repository.BarcodeRepository

private val LAYOUTS = arrayOf(
    R.layout.fragment_result_detail,
    R.layout.fragment_result_questionnaire,
    R.layout.fragment_result_location
)

class ResultViewModel(
    private val barcodeRepository: BarcodeRepository
): ViewModel() {
    private val _index = MutableLiveData<Int>()
    val text: LiveData<String> = Transformations.map(_index){
        "Index $it"
    }
    val layout: LiveData<Int> = Transformations.map(_index){
        LAYOUTS[it]
    }
    fun setIndex(index: Int) {
        _index.postValue(index)
    }
    val barcode: LiveData<String> = barcodeRepository.code()
    val details: LiveData<BarcodeDetailResponse> = barcodeRepository.get()
    fun setDetail(code: String, detailResponse: BarcodeDetailResponse){
        barcodeRepository.set(code, detailResponse)
    }
    fun clear() = barcodeRepository.clear()
}