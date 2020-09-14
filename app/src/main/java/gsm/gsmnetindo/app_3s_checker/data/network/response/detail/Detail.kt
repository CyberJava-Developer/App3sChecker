package gsm.gsmnetindo.app_3s_checker.data.network.response.detail


import com.google.gson.annotations.SerializedName

data class Detail(
    @SerializedName("born_date")
    val bornDate: String,
    @SerializedName("born_place")
    val bornPlace: String,
    @SerializedName("created_at")
    val createdAt: String,
    val gender: String,
    val name: String,
    @SerializedName("updated_at")
    val updatedAt: String?
)