package gsm.gsmnetindo.app_3s_checker.data.network.response.barcode


import com.google.gson.annotations.SerializedName

data class BarcodeDetailResponse(
    val account: Account,
    val history: List<History>,
    val status: Status,
    val user: User
)