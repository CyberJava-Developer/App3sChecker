package gsm.gsmnetindo.app_3s_checker.data.network.response.observation


import com.google.gson.annotations.SerializedName

data class ObservationResponseItem(
    @SerializedName("created_at")
    val createdAt: String,
    val latitude: Double,
    val longitude: Double,
    @SerializedName("updated_at")
    val updatedAt: String,
    val user: User
)