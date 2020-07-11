package gsm.gsmnetindo.app_3s_checker.data.network.response.barcode


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("created_at")
    val createdAt: String,
    val latitude: String,
    val longitude: String,
    @SerializedName("updated_at")
    val updatedAt: String
)