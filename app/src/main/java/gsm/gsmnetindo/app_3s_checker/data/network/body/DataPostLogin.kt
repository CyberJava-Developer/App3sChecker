package gsm.gsmnetindo.app_3s_checker.data.network.body

import com.google.gson.annotations.SerializedName

data class DataPostLogin(
    @SerializedName("phone") var phone: String,
    @SerializedName("app_hash") var appHash: String
)