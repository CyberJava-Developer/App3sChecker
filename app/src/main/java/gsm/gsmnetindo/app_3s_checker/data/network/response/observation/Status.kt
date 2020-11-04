package gsm.gsmnetindo.app_3s_checker.data.network.response.observation


import com.google.gson.annotations.SerializedName

data class Status(
    val authority: Int?,
    @SerializedName("created_at")
    val createdAt: String,
    val id: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("accuracy")
    val accuracy: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    val user: Int,
    val verified: Boolean?
)