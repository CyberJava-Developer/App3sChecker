package gsm.gsmnetindo.app_3s_checker.data.network.response.barcode


data class BarcodeDetailResponse(
    val account: Account,
    val history: List<History>,
    val status: Status,
    val kuesioner: Kuesioner,
    val user: User,
    val location: List<Location>
)