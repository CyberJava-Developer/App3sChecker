package gsm.gsmnetindo.app_3s_checker.data.network.response.observation


import com.google.gson.annotations.SerializedName

data class User(
    val account: Account,
    @SerializedName("born_date")
    val bornDate: String,
    @SerializedName("born_place")
    val bornPlace: String,
    @SerializedName("created_at")
    val createdAt: String,
    val gender: String,
    val home: String,
    val id: Int,
    val location: String,
    val name: String,
    val status: Status,
    @SerializedName("updated_at")
    val updatedAt: String
)