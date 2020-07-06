package gsm.gsmnetindo.app_3s_checker.data.network.response.barcode


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("born_date")
    val bornDate: String,
    @SerializedName("born_place")
    val bornPlace: String,
    @SerializedName("created_at")
    val createdAt: String,
    val gender: String,
    val name: String,
    @SerializedName("updated_at")
    val updatedAt: String
)