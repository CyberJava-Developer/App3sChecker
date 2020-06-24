package gsm.gsmnetindo.app_3s_checker.data.network.response


import com.google.gson.annotations.SerializedName

data class VersionResponse(
    @SerializedName("app_name")
    val appName: String,
    @SerializedName("app_version_code")
    val appVersionCode: Int,
    @SerializedName("app_version_name")
    val appVersionName: String
)