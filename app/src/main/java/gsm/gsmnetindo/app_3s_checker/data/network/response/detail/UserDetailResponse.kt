package gsm.gsmnetindo.app_3s_checker.data.network.response.detail


import com.google.gson.annotations.SerializedName

data class UserDetailResponse(
    val account: Account,
    val detail: Detail,
    val home: Home,
    val kuesioner: Kuesioner,
    val location: Location,
    val status: Status,
    val subscription: Subscription
)